package io.avec.crud.place;

import io.avec.crud.data.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Place extends AbstractEntity {

    @NotNull
    @Column(unique = true)
    private String place;

    private String description;

    @NotNull
    private double lon;

    @NotNull
    private double lat;
}
