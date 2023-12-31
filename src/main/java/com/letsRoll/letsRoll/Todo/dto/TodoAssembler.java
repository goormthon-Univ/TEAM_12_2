package com.letsRoll.letsRoll.Todo.dto;

import com.letsRoll.letsRoll.Goal.entity.Goal;
import com.letsRoll.letsRoll.Member.entity.Member;
import com.letsRoll.letsRoll.Todo.dto.res.*;
import com.letsRoll.letsRoll.Todo.entity.Todo;
import com.letsRoll.letsRoll.Todo.entity.TodoEndManager;
import com.letsRoll.letsRoll.Todo.entity.TodoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TodoAssembler {

    public Todo toTodoEntity(Goal goal, TodoManager todoManager, String content, LocalDate endDate) {

        return Todo.builder()
                .goal(goal)
                .todoManager(todoManager)
                .content(content)
                .endDate(endDate)
                .build();
    }

    public TodoManager toTodoManagerEntity(Member member) {
        return TodoManager.builder()
                .member(member)
                .build();
    }
    public TodoEndManager toTodoEndManagerEntity(Member member, List<Todo> todoList) {
        return TodoEndManager.builder()
                .member(member)
                .todoList(todoList)
                .build();
    }

    public TodoListResDto toDateTodoListResDtoEntity(Todo todo) {
        return TodoListResDto.builder()
                .goalId(todo.getGoal().getId())
                .goalContent(todo.getGoal().getContent())
                .todoId(todo.getId())
                .todoContent(todo.getContent())
                .todoManagerMemberId(todo.getTodoManager().getMember().getId())
                .todoManagerNickName(todo.getTodoManager().getMember().getNickname())
                .isComplete(todo.getIsComplete())
                .todoEndDate(todo.getEndDate())
                .build();
    }

    public MyTodoResDto toMyTodoResDtoEntity(Todo todo) {
        return MyTodoResDto.builder()
                .goalId(todo.getGoal().getId())
                .goalContent(todo.getGoal().getContent())
                .todoId(todo.getId())
                .todoContent(todo.getContent())
                .todoManagerMemberId(todo.getTodoManager().getMember().getId())
                .todoManagerNickName(todo.getTodoManager().getMember().getNickname())
                .isComplete(todo.getIsComplete())
                .build();
    }

    public MonthlyCheckTodoListResDto toMonthlyCheckEntity(List<LocalDate> completeDates, List<LocalDate> inCompleteDates) {
        return MonthlyCheckTodoListResDto.builder()
                .allCompleteDateList(completeDates)
                .inCompleteDateList(inCompleteDates)
                .build();
    }

    public ReportTodo reportTodo(Member member, int completeCount, int managedCount) {
        return ReportTodo.builder()
                .memberId(member.getId())
                .memberNickName(member.getNickname())
                .completeCount(completeCount)
                .managedCount(managedCount)
                .build();
    }

    public AllReportTodo allReportTodo(int count, List<ReportTodo> reportTodoList) {
        return AllReportTodo.builder()
                .todoCount(count)
                .reportTodoList(reportTodoList)
                .build();
    }
}
