package com.springboot.blog.payload;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data 
public class PostDTO {
    private long id;

    // Title must not null nor empty, and limit 2 character words (@Size)
    @NotEmpty
    @Size(min = 2, message = "Post title must include at least 2 characters")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post description must include at least 10 characters")
    private String description;
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;

}
