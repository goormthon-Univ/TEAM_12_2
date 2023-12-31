package com.letsRoll.letsRoll.Comment_Feeling.repository;

import com.letsRoll.letsRoll.Comment_Feeling.entity.Comment;
import com.letsRoll.letsRoll.Goal.entity.Goal;
import com.letsRoll.letsRoll.Todo.entity.Todo;
import com.letsRoll.letsRoll.global.enums.CommentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // todo 코멘트 리스트
    List<Comment> findAllByGoalAndTodoAndTypeOrderByCreatedDateAsc(Goal goal, Todo todo, CommentType commentType);
    List<Comment> findAllByGoalAndTypeOrderByCreatedDateAsc(Goal goal, CommentType commentType);

    Optional<Comment> findCommentByIdAndTodoAndType(Long commentId, Todo todo, CommentType type);

    Optional<Comment> findCommentByIdAndGoalAndType(Long commentId, Goal goal, CommentType type);

    Optional<Comment> findCommentByGoalAndType(Goal goal, CommentType commentType);

}
