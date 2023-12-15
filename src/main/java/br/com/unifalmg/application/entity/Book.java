package br.com.unifalmg.application.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "db", name = "book")
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer publication_year;
    private Integer pages;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    //@JsonManagedReference
    //@JsonBackReference
    private Author author;


}
