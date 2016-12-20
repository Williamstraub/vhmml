alter table lexicon_terms drop index term;
create index lexicon_terms_idx1 ON lexicon_terms(term);