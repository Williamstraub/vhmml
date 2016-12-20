package org.vhmml.entity.readingroom;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "MANUSCRIPT_PRINT")
public class ManuscriptAndPrinted extends ReadingRoomObject {

}
