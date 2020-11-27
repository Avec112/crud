package io.avec.crud.place;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Place {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true)
    private String place;

    private String description;

    @NotNull
    private double lon;

    @NotNull
    private double lat;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Place place1 = (Place) o;

        return new EqualsBuilder()
                .append(lon, place1.lon)
                .append(lat, place1.lat)
                .append(id, place1.id)
                .append(place, place1.place)
                .append(description, place1.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(place)
                .append(description)
                .append(lon)
                .append(lat)
                .toHashCode();
    }
}
