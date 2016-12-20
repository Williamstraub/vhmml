package org.vhmml.entity.readingroom;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "PRINT")
public class PrintedObject extends ReadingRoomObject {

}
