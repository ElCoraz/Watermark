package com.elcorazon.adminlte.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**********************************************************************************************************************/
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Search {
    /******************************************************************************************************************/
    @JsonProperty("search")
    public String search;
}