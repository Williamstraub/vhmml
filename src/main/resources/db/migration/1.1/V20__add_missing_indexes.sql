CREATE INDEX lexicon_related_terms_idx1 ON lexicon_related_terms(term_id);

CREATE INDEX lexicon_related_terms_idx2 ON lexicon_related_terms(related_term_id);

CREATE INDEX lexicon_contributors_idx1 ON lexicon_contributors(contributor_id);

CREATE INDEX lexicon_contributors_idx2 ON lexicon_contributors(lexicon_id);

CREATE INDEX reference_tags_idx1 ON reference_tags(reference_entry_id);

CREATE INDEX reference_tags_idx2 ON reference_tags(tag_id);