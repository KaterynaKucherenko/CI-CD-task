package com.mjc.school.repository.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Component
@Table(name = "comment")
@AllArgsConstructor
public class CommentModel implements BaseEntity <Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "content")
    @Size(min = 5, max = 255)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private NewsModel newsModel;

    @CreatedDate
    @Column(name = "created")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "M/d/yy", "dd.MM.yyyy" })
    private LocalDateTime created;
    @LastModifiedDate
    @Column(name = "modified")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "M/d/yy", "dd.MM.yyyy" })
    private LocalDateTime modified;


    public CommentModel() {

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public NewsModel getNewsModel() {
        return newsModel;
    }

    public void setNewsModel(NewsModel newsModel) {
        this.newsModel = newsModel;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        CommentModel authorModel = (CommentModel) obj;
        return id == authorModel.id &&
                (content == authorModel.content || (content != null && content.equals(authorModel.getContent()))) &&
                (created == authorModel.created || (created != null && created.equals(authorModel.getCreated()))) &&
                (modified == authorModel.modified || (modified != null && modified.equals(authorModel.getModified())));
    }

    public int hashCode() {
        return Objects.hash(id, content, created, modified);
    }

    public String toString() {
        return "Comment's ID: " + id + ", content: " + content + ", created: " + created + ", modified: " + modified;
    }
}
