package com.consort.entities;

import com.fasterxml.jackson.annotation.*;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * PropertyInfo
 */

public class PropertyInfo {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("isArray")
  private Boolean isArray = null;

  @JsonProperty("isRequired")
  private Boolean isRequired = null;

  public PropertyInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of the property.
   * 
   * @return name
   **/
  @NotNull
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PropertyInfo type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Type of the property. Can be any primitive type defined in swagger spec or
   * the name of another domain model
   * 
   * @return type
   **/
  @NotNull
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public PropertyInfo isArray(Boolean isArray) {
    this.isArray = isArray;
    return this;
  }

  /**
   * Indicates if this property is an array
   * 
   * @return isArray
   **/
  @NotNull
  public Boolean isIsArray() {
    return isArray;
  }

  public void setIsArray(Boolean isArray) {
    this.isArray = isArray;
  }

  public PropertyInfo isRequired(Boolean isRequired) {
    this.isRequired = isRequired;
    return this;
  }

  /**
   * Indicates if this property is mandatory
   * 
   * @return isRequired
   **/
  @NotNull
  public Boolean isIsRequired() {
    return isRequired;
  }

  public void setIsRequired(Boolean isRequired) {
    this.isRequired = isRequired;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropertyInfo propertyInfo = (PropertyInfo) o;
    return Objects.equals(name, propertyInfo.name) && Objects.equals(type, propertyInfo.type)
        && Objects.equals(isArray, propertyInfo.isArray) && Objects.equals(isRequired, propertyInfo.isRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type, isArray, isRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropertyInfo {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    isArray: ").append(toIndentedString(isArray)).append("\n");
    sb.append("    isRequired: ").append(toIndentedString(isRequired)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
