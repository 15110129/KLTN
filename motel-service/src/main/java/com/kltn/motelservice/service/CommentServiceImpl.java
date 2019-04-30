package com.kltn.motelservice.service;

import com.kltn.motelservice.entity.Comment;
import com.kltn.motelservice.entity.Post;
import com.kltn.motelservice.entity.User;
import com.kltn.motelservice.exception.CommentException;
import com.kltn.motelservice.exception.PostException;
import com.kltn.motelservice.model.CommentDTO;
import com.kltn.motelservice.repository.CommentRepository;
import com.kltn.motelservice.repository.PostRepository;
import com.kltn.motelservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        try {
            Optional<Post> post = postRepository.findById(commentDTO.getIdPost());
            if (post.isPresent()) {
                Comment comment = new Comment();
                comment.setContent(commentDTO.getContent());
                comment.setLastUpdate(LocalDateTime.now());
                Optional<User> user = userRepository.findById(commentDTO.getIdUser());
                comment.setPost(post.get());
                comment.setUser(user.get());
                commentRepository.save(comment);
                commentDTO = modelMapper.map(comment, CommentDTO.class);
                commentDTO.setIdUser(comment.getUser().getId());
                commentDTO.setFullName(comment.getUser().getFullName());
                return commentDTO;
            } else
                throw new PostException("Không tồn tại post id " + commentDTO.getIdPost());
        } catch (Exception e) {
            throw new CommentException("Xảy ra lỗi khi thêm bình luận ", e);
        }
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (!comment.isPresent())
                throw new CommentException("Comment id " + id + "không tồn tại!!!");
            else {
                commentDTO.setId(id);
                comment.get().setContent(commentDTO.getContent());
                comment.get().setLastUpdate(LocalDateTime.now());
                commentRepository.save(comment.get());
                commentDTO = modelMapper.map(comment.get(), CommentDTO.class);
                return commentDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteComment(Long id) {
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (!comment.isPresent())
                throw new CommentException("Comment id " + id + "không tồn tại!!!");
            else {
                commentRepository.delete(comment.get());
                return "Đã xóa comment id " + id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Page<CommentDTO> getCommentByIdPost(Long idPost, int page) {
        try {
            Optional<Post> post = postRepository.findById(idPost);
            if (post.isPresent()) {
                Page<Comment> commentPage = commentRepository.findAllByPost(post.get(), PageRequest.of(page, 10, Sort.by("lastUpdate")));
                Page<CommentDTO> commentDTOPage = commentPage.map(comment -> {
                    CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
                    commentDTO.setIdUser(comment.getUser().getId());
                    commentDTO.setFullName(comment.getUser().getFullName());
                    return commentDTO;
                });

                return commentDTOPage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
