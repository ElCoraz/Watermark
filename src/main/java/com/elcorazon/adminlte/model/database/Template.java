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
public class Template {
    /******************************************************************************************************************/
    @Id
    @Column(nullable = false)
    public String id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String top;
    @Column(nullable = false)
    public String bottom;
}
