package com.elcorazon.adminlte.model.database;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**********************************************************************************************************************/
@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Watermark {
    /******************************************************************************************************************/
    @Id
    @Column(nullable = false)
    public String id;
    @Column(nullable = false)
    public String url;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String uuid;
    @Column(nullable = false)
    public String value;
    @Column(nullable = false)
    public String template;
}
