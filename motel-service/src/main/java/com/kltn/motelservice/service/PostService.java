package com.kltn.motelservice.service;

import com.kltn.motelservice.model.PostDTO;
import com.kltn.motelservice.model.SearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPost();

    List<PostDTO> getPostByApproved(boolean bool);

//    Page<PostDTO> getPostByApproved(boolean bool, int page);

    List<PostDTO> getPostByUsername(String username);

    PostDTO getPostById(Long id);

    PostDTO createPost(PostDTO postDTO);

    PostDTO updatePost(Long id, PostDTO postDTO);

    PostDTO deletePost(Long id);

    List<PostDTO> getMotelPost(boolean bool);

//    Page<PostDTO> getMotelPost(boolean bool, int page);

    String deletePostByAdmin(Long id);

    PostDTO ApprovePost(Long id, boolean isApprove);

    List<PostDTO> searchPost(SearchDTO searchForm);

//    Page<PostDTO> searchPost(SearchDTO searchForm, int page);

    List<PostDTO> searchPostByMaps(SearchDTO searchForm);
}
