package com.consort.entities;

import com.fasterxml.jackson.annotation.*;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * ErrorResponse
 */

public class ErrorResponse {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("message")
  private String message = null;

  public ErrorResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * This error code should have the format 'DMS-XXX' where XXX is an integer. It
   * is used to uniquely distinguish different error cases in order to display a
   * suitable and translatable error message on the UI.
   * 
   * @return code
   **/
  @NotNull
  @Pattern(regexp = "^DMS-\\d+$")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Should contain a short, meaningful description of the error case. Might be
   * displayed to the end user.
   * 
   * @return message
   **/
  @NotNull
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(code, errorResponse.code) && Objects.equals(message, errorResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorResponse {\n");

    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
