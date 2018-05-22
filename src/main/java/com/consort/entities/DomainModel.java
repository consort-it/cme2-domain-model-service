package com.consort.entities;

import com.consort.entities.PropertyInfo;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.*;
import javax.validation.*;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * DomainModel
 */
public class DomainModel {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("serviceName")
  private String serviceName = null;

  @JsonProperty("properties")
  private List<PropertyInfo> properties = new ArrayList<>();

  @JsonProperty("headerColor")
  private String headerColor = null;

  @JsonProperty("iconName")
  private String iconName = null;

  @JsonProperty("positionX")
  private Integer positionX = null;

  @JsonProperty("positionY")
  private Integer positionY = null;

  @JsonProperty("zIndex")
  private Integer zIndex = null;

  public DomainModel id(String id) {
    this.id = id;
    return this;
  }

  /**
   * unique id of the domain model
   * 
   * @return id
   **/
  @NotNull
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DomainModel name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of the model object
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

  public DomainModel serviceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  /**
   * name of the microservice the model belongs to
   * 
   * @return serviceName
   **/
  @NotNull
  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public DomainModel properties(List<PropertyInfo> properties) {
    this.properties = properties;
    return this;
  }

  public DomainModel addPropertiesItem(PropertyInfo propertiesItem) {
    properties.add(propertiesItem);
    return this;
  }

  /**
   * Get properties
   * 
   * @return properties
   **/
  @NotNull
  @Valid
  public List<PropertyInfo> getProperties() {
    return properties;
  }

  public void setProperties(List<PropertyInfo> properties) {
    this.properties = properties;
  }

  public DomainModel headerColor(String headerColor) {
    this.headerColor = headerColor;
    return this;
  }

  /**
   * Specifies the color of the header (as valid html color)
   * 
   * @return headerColor
   **/
  public String getHeaderColor() {
    return headerColor;
  }

  public void setHeaderColor(String headerColor) {
    this.headerColor = headerColor;
  }

  public DomainModel iconName(String iconName) {
    this.iconName = iconName;
    return this;
  }

  /**
   * Specifies the name of a google material icon which should be displayed on
   * this model (optional). For full list see https://material.io/tools/icons/
   * 
   * @return iconName
   **/
  public String getIconName() {
    return iconName;
  }

  public void setIconName(String iconName) {
    this.iconName = iconName;
  }

  public DomainModel positionX(Integer positionX) {
    this.positionX = positionX;
    return this;
  }

  /**
   * The horizontal position of the Model object on the viewport
   * 
   * @return positionX
   **/
  public Integer getPositionX() {
    return positionX;
  }

  public void setPositionX(Integer positionX) {
    this.positionX = positionX;
  }

  public DomainModel positionY(Integer positionY) {
    this.positionY = positionY;
    return this;
  }

  /**
   * The vertical position of the Model object on the viewport
   * 
   * @return positionY
   **/
  public Integer getPositionY() {
    return positionY;
  }

  public void setPositionY(Integer positionY) {
    this.positionY = positionY;
  }

  public DomainModel zIndex(Integer zIndex) {
    this.zIndex = zIndex;
    return this;
  }

  public Integer getZIndex() {
    return zIndex;
  }

  public void setZIndex(Integer zIndex) {
    this.zIndex = zIndex;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DomainModel domainModel = (DomainModel) o;
    return Objects.equals(id, domainModel.id) && Objects.equals(name, domainModel.name)
        && Objects.equals(serviceName, domainModel.serviceName) && Objects.equals(properties, domainModel.properties)
        && Objects.equals(headerColor, domainModel.headerColor) && Objects.equals(iconName, domainModel.iconName)
        && Objects.equals(positionX, domainModel.positionX) && Objects.equals(positionY, domainModel.positionY)
        && Objects.equals(zIndex, domainModel.zIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, serviceName, properties, headerColor, iconName, positionX, positionY, zIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DomainModel {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    serviceName: ").append(toIndentedString(serviceName)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    headerColor: ").append(toIndentedString(headerColor)).append("\n");
    sb.append("    iconName: ").append(toIndentedString(iconName)).append("\n");
    sb.append("    positionX: ").append(toIndentedString(positionX)).append("\n");
    sb.append("    positionY: ").append(toIndentedString(positionY)).append("\n");
    sb.append("    zIndex: ").append(toIndentedString(zIndex)).append("\n");
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
