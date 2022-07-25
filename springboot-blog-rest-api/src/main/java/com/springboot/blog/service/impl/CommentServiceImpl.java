package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;


    // Constructor Injection
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }
    //Merge together
//    public CommentServiceImpl(PostRepository postRepository) {
//        this.postRepository = postRepository;
//    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // Retrieve post entity By Id
        Post post = postRepository.findById(postId).orElseThrow(
                () ->new ResourceNotFoundException("post", "Id", postId));

        // Set post to Entity
        comment.setPost(post);

        // Comment Entity to DB
        Comment newComment = commentRepository.save(comment);


        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        // Retrieve comments By postId
        List<Comment> comments = commentRepository.findCommentByPostId(postId);

        // Convert List<Comment> to List<CommentDto>
        // Java8 stream().map() to map Entity to DTO and Call mapToDTO to pass the comments Entity
        // Last is Collect the result here By collect(Collector.toList)
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // Retrieve post entity By Id
        Post post = postRepository.findById(postId).orElseThrow(
                () ->new ResourceNotFoundException("post", "Id", postId));

        // Retrieve comment By Id
        Comment coment = commentRepository.findById(commentId).orElseThrow(() ->
                 new ResourceNotFoundException("comment", "Id", commentId));

        // Check  the comment is belong to particular post or throw Exception
        if(!coment.getPost().getId().equals(post.getId())){
           throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to the post!");
        }

        return mapToDTO(coment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("post", "Id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("comment", "Id", commentId));
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post!");
        }
          comment.setName(commentRequest.getName());
          comment.setEmail(commentRequest.getEmail());
          comment.setBody(commentRequest.getBody());
          Comment updateComment = commentRepository.save(comment);
        return mapToDTO(updateComment);
    }

    @Override
    public void deleteComment( Long commentId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("post", "Id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->
                new ResourceNotFoundException("comment", "Id", commentId));
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post!");
        }
       commentRepository.deleteById(commentId); // because void deleteById, so just commentRepository.deleteById

    }

    // DTO Transfer (DTO<-->Entity) create private method
    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        comment.setName(comment.getName());
//        comment.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }

}
