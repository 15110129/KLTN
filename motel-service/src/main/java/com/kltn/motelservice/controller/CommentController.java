package com.kltn.motelservice.controller;

import com.kltn.motelservice.entity.Comment;
import com.kltn.motelservice.model.CommentDTO;
import com.kltn.motelservice.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
//@PreAuthorize("#oauth2.hasAnyScope('read')")
public class CommentController {
    @Autowired
    CommentServiceImpl commentService;

    @GetMapping("/comments/post/{id}")
    public Page<CommentDTO> getComment(@PathVariable Long id, @RequestParam("page") int page) {
        return commentService.getCommentByIdPost(id, page);
    }

    @PostMapping("/comment/post")
    @PreAuthorize("#oauth2.hasAnyScope('read')")
    public CommentDTO createComment(@RequestBody CommentDTO commentDTO, OAuth2Authentication auth) {
        return commentService.createComment(commentDTO, auth.getName());
    }

    @PutMapping("/comment/{id}")
    @PreAuthorize("#oauth2.hasAnyScope('read')")
    public CommentDTO updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO, OAuth2Authentication auth) {
        return commentService.updateComment(id, commentDTO, auth.getName());
    }

    @DeleteMapping("/comment/{id}")
    @PreAuthorize("#oauth2.hasAnyScope('read')")
    public String deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}