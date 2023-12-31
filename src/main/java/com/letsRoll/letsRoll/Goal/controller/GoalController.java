package com.letsRoll.letsRoll.Goal.controller;

import com.letsRoll.letsRoll.Goal.dto.req.GoalAgreeReq;
import com.letsRoll.letsRoll.Goal.dto.res.CheckGoalAgree;
import com.letsRoll.letsRoll.Goal.dto.res.GoalResDto;
import com.letsRoll.letsRoll.Goal.dto.req.GoalAddReq;
import com.letsRoll.letsRoll.Goal.dto.res.ReportGoalResDto;
import com.letsRoll.letsRoll.Goal.dto.res.TimeLineResDto;
import com.letsRoll.letsRoll.Goal.service.GoalAgreeService;
import com.letsRoll.letsRoll.Goal.service.GoalService;
import com.letsRoll.letsRoll.Project.dto.res.ProjectResDto;
import com.letsRoll.letsRoll.Todo.dto.res.TodoListResDto;
import com.letsRoll.letsRoll.Todo.service.TodoService;
import com.letsRoll.letsRoll.global.common.BaseResponse;
import com.letsRoll.letsRoll.global.exception.BaseResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    private final GoalAgreeService goalAgreeService;
    private final TodoService todoService;

    @PostMapping("/{projectId}")
    public BaseResponse<Void> addGoal(@PathVariable Long projectId, @RequestBody @Valid GoalAddReq goalAddReq) {
        goalService.addGoal(projectId, goalAddReq);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @GetMapping("/{goalId}")
    public BaseResponse<GoalResDto> getGoalDetails(@PathVariable Long goalId) {
        GoalResDto goalDetails = goalService.getGoalDetails(goalId);
        return new BaseResponse<>(goalDetails);
    }

    @PostMapping("/{goalId}/agree")
    public BaseResponse<Void> agreeGoal(@PathVariable Long goalId, @RequestBody @Valid GoalAgreeReq goalAgreeReq) {
        goalAgreeService.agreeGoal(goalId, goalAgreeReq.getMemberId());
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @PostMapping("/{goalId}/complete")
    public BaseResponse<Void> completeGoal(@PathVariable Long goalId) {
        goalService.completeGoal(goalId);
        return new BaseResponse<>(BaseResponseCode.SUCCESS);
    }

    @GetMapping("/{goalId}/todos")
    public BaseResponse<List<TodoListResDto>> getTodoListByGoal(@PathVariable Long goalId) {
        return new BaseResponse<>(todoService.getTodoListByGoal(goalId));
    }

    @GetMapping("/{goalId}/timeline")
    public BaseResponse<TimeLineResDto> getTimeLine(@PathVariable Long goalId) {
        return new BaseResponse<>(goalService.getTimeLine(goalId));
    }

    @GetMapping("/report/{projectId}")
    public BaseResponse<List<ReportGoalResDto>> getReportGoal(@PathVariable Long projectId) {
        return new BaseResponse<>(goalService.getReportGoal(projectId));
    }

    @GetMapping("/{goalId}/check")
    public BaseResponse<CheckGoalAgree> checkGoalAgree(@PathVariable Long goalId) {
        return new BaseResponse<>(goalService.checkGoalAgree(goalId));
    }
}
