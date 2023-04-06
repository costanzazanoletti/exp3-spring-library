package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import org.learning.springlibrary.model.Category;
import org.learning.springlibrary.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public String index(Model model) {
    model.addAttribute("list", categoryService.getAll());
    model.addAttribute("categoryObj", new Category());
    return "/categories/index";
  }

  @PostMapping("/save")
  public String doSave(@Valid @ModelAttribute(name = "categoryObj") Category category,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      // ricreo la view ripassando la category
      model.addAttribute("list", categoryService.getAll());
      return "/categories/index";
    }
    // salvo i dati
    categoryService.create(category);
    return "redirect:/categories";
  }
}
