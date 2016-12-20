package org.vhmml.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "lexicon_related_terms")
public class LexiconRelatedTerm {

	@Id
	@GeneratedValue
	private Long id;	

	@Column(name = "term_id")
	private Long termId;
	
	@Column(name = "related_term_id")
	private Long relatedTermId;
}
