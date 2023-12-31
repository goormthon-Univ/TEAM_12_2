package com.letsRoll.letsRoll.Goal.service;

import com.letsRoll.letsRoll.Comment_Feeling.dto.CommentAssembler;
import com.letsRoll.letsRoll.Comment_Feeling.dto.res.CommentInfoDto;
import com.letsRoll.letsRoll.Comment_Feeling.dto.res.TodoCommentResDto;
import com.letsRoll.letsRoll.Comment_Feeling.entity.Comment;
import com.letsRoll.letsRoll.Comment_Feeling.entity.Feeling;
import com.letsRoll.letsRoll.Comment_Feeling.repository.CommentRepository;
import com.letsRoll.letsRoll.Comment_Feeling.repository.FeelingRepository;
import com.letsRoll.letsRoll.Goal.dto.GoalAssembler;
import com.letsRoll.letsRoll.Goal.dto.req.GoalAddReq;
import com.letsRoll.letsRoll.Goal.dto.res.CheckGoalAgree;
import com.letsRoll.letsRoll.Goal.dto.res.GoalResDto;
import com.letsRoll.letsRoll.Goal.dto.res.ReportGoalResDto;
import com.letsRoll.letsRoll.Goal.dto.res.TimeLineResDto;
import com.letsRoll.letsRoll.Goal.entity.Goal;
import com.letsRoll.letsRoll.Goal.entity.GoalAgree;
import com.letsRoll.letsRoll.Goal.repository.GoalAgreeRepository;
import com.letsRoll.letsRoll.Goal.repository.GoalRepository;
import com.letsRoll.letsRoll.Member.entity.Member;
import com.letsRoll.letsRoll.Member.repository.MemberRepository;
import com.letsRoll.letsRoll.Project.entity.Project;
import com.letsRoll.letsRoll.Project.repository.ProjectRepository;
import com.letsRoll.letsRoll.Todo.entity.Todo;
import com.letsRoll.letsRoll.Todo.repository.TodoRepository;
import com.letsRoll.letsRoll.global.enums.CommentType;
import com.letsRoll.letsRoll.global.exception.BaseException;
import com.letsRoll.letsRoll.global.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;
    private final ProjectRepository projectRepository;
    private final GoalAgreeRepository goalAgreeRepository;
    private final CommentRepository commentRepository;
    private final CommentAssembler commentAssembler;
    private final FeelingRepository feelingRepository;
    private final TodoRepository todoRepository;
    private final GoalAssembler goalAssembler;
    private final MemberRepository memberRepository;

    public void addGoal(Long projectId, GoalAddReq goalAddReq) {


        // 프로젝트 정보 가져오기
        Project project = getProject(projectId);
        // 요청한 user와 project에 대해 member 정보 가져오기
        Member mem = memberRepository.findMemberByProjectAndUserId(project,goalAddReq.getUserId())
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_MEMBER));
        if(!project.getMembers().contains(mem)) {
            throw new BaseException(BaseResponseCode.NOT_FOUND_MEMBER);
        }

        Goal goal = goalRepository.save(goalAssembler.goal(goalAddReq, project));

        for (Member member : project.getMembers()) {
            // GoalAgree 정보 저장
            GoalAgree goalAgree = goalAssembler.goalAgree(goal, member);
            goalAgreeRepository.save(goalAgree);
        }

    }

    public GoalResDto getGoalDetails(Long goalId) {
        Goal goal = getGoal(goalId);
        return GoalAssembler.fromEntity(goal);
    }

    public void completeGoal(Long goalId) {
        Goal goal = getGoal(goalId);
        for(GoalAgree goalAgree : goal.getGoalAgreeList()) {
            if(!goalAgree.getMemberCheck()) {
                throw new BaseException(BaseResponseCode.NOT_COMPLETED_GOAL);
            }
    }
        goal.setIsComplete(true);
        goal.setFinishDate(LocalDate.now());
        goalRepository.save(goal);
    }
    public TimeLineResDto getTimeLine(Long goalId) {
        Goal goal = getGoal(goalId);

        List<CommentInfoDto> goalCommentList = getComments(commentRepository.findAllByGoalAndTypeOrderByCreatedDateAsc(goal, CommentType.GOAL));
        List<Todo> todoList = todoRepository.findTodosByGoalAndIsCompleteIsTrueOrderByCreatedDate(goal);
        List<TodoCommentResDto> todoCommentList = new ArrayList<>();

        for (Todo todo : todoList) {
            List<CommentInfoDto> todoCommentInfo = getComments(commentRepository.findAllByGoalAndTodoAndTypeOrderByCreatedDateAsc(goal, todo, CommentType.TODO));
            todoCommentList.add(commentAssembler.toTodoCommentResDtoEntity(todo, todoCommentInfo));
        }
        return TimeLineResDto.builder().goalCommentList(goalCommentList).todoCommentList(todoCommentList).build();
    }

    public List<CommentInfoDto> getComments(List<Comment> comments) {
        List<CommentInfoDto> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            List<Feeling> feelingList = feelingRepository.findAllByComment(comment);

            List<Long> memberList = new ArrayList<>();
            feelingList.forEach(feeling -> memberList.add(feeling.getMember().getId()));

            commentList.add(commentAssembler.toCommentInfoDtoEntity(comment.getCreatedDate(),comment.getContent(), comment.getMember().getId(),
                    memberList.size(), memberList));
        }
        return commentList;
    }

    public List<ReportGoalResDto> getReportGoal(Long projectId) {
        Project project = getProject(projectId);
        List<Goal> goalList = project.getGoals();
        List<ReportGoalResDto> reportGoalResDtoList = new ArrayList<>();

        for (Goal goal : goalList) {
            reportGoalResDtoList.add(goalAssembler.reportGoalResDto(project, goal));
        }

        return reportGoalResDtoList;
    }

    public CheckGoalAgree checkGoalAgree(Long goalId) {
        Goal goal = getGoal(goalId);

        if (todoRepository.findTodosByGoalAndIsCompleteIsFalse(goal).isEmpty()) {
            List<Member> notCheckMembers = goal.getGoalAgreeList().stream()
                    .filter(goalAgree -> !goalAgree.getMemberCheck())
                    .map(GoalAgree::getMember).toList();
            return goalAssembler.checkGoalAgree(goal, notCheckMembers);
        } else
            throw new BaseException(BaseResponseCode.NOT_ALL_TODOS_COMPLETE);
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_PROJECT));
    }

    public Goal getGoal(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_GOAL));
    }


}
