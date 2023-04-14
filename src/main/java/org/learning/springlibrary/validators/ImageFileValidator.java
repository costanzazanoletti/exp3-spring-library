package org.learning.springlibrary.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {

  @Override
  public void initialize(ValidImageFile constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(MultipartFile multipartFile,
      ConstraintValidatorContext constraintValidatorContext) {

    // if file is null or empty
    // if file content type is not an image
    return (multipartFile != null && !multipartFile.isEmpty() && imageContentType(
        multipartFile.getContentType()));


  }

  private boolean imageContentType(String contentType) {
    String[] contentTypeSplit = contentType.split("/");
    return contentTypeSplit[0].equalsIgnoreCase("image") && (
        contentTypeSplit[1].equalsIgnoreCase("png")
            || contentTypeSplit[1].equalsIgnoreCase("jpeg")
    );
  }
}
