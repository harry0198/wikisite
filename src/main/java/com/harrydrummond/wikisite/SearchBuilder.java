package com.harrydrummond.wikisite;

import com.harrydrummond.wikisite.knowledgebase.Tag;
import java.util.*;

public class SearchBuilder {

    private String searchString;
    private Set<Tag> tagFilter = new HashSet<>();

    /**
     * String used to search directory with to produce results.
     * @param searchString String to search for
     * @return This class
     */
    public SearchBuilder setSearchString(String searchString) {
        this.searchString = searchString;
        return this;
    }

    /**
     * Adds tags to filter search result down to.
     * @param tags Tag(s) to add
     * @return This class
     */
    public SearchBuilder addTagFilter(Tag... tags) {
        if (tags == null) return this;
        this.tagFilter.addAll(Arrays.asList(tags));
        return this;
    }

    public Set<Tag> getTagFilter() {
        return tagFilter;
    }

    public String getSearchString() {
        return searchString;
    }
}