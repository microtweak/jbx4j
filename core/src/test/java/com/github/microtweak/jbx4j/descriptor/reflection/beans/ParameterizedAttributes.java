package com.github.microtweak.jbx4j.descriptor.reflection.beans;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterizedAttributes {

    private Long withoutTypeParameter;

    private List<String> singleTypeParameterList;

    private Set<String> singleTypeParameterSet;

    private Map<Integer, String> pairTypeParameter;

    public Long getWithoutTypeParameter() {
        return withoutTypeParameter;
    }

    public void setWithoutTypeParameter(Long withoutTypeParameter) {
        this.withoutTypeParameter = withoutTypeParameter;
    }

    public List<String> getSingleTypeParameterList() {
        return singleTypeParameterList;
    }

    public void setSingleTypeParameterList(List<String> singleTypeParameterList) {
        this.singleTypeParameterList = singleTypeParameterList;
    }

    public Set<String> getSingleTypeParameterSet() {
        return singleTypeParameterSet;
    }

    public void setSingleTypeParameterSet(Set<String> singleTypeParameterSet) {
        this.singleTypeParameterSet = singleTypeParameterSet;
    }

    public Map<Integer, String> getPairTypeParameter() {
        return pairTypeParameter;
    }

    public void setPairTypeParameter(Map<Integer, String> pairTypeParameter) {
        this.pairTypeParameter = pairTypeParameter;
    }
}