package org.vhmml.entity.readingroom;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MANUSCRIPT")
public class Manuscript extends ReadingRoomObject {

}
