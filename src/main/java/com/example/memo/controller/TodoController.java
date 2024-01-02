package com.example.memo.controller;

import com.example.memo.entity.Todo;
import com.example.memo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;
    @GetMapping("/")
    public String list(Model model) {
        List<Todo> todos = todoRepository.findAll();
        model.addAttribute("todos", todos);
        model.addAttribute("newTodo", new Todo());
        return "todo/list";
    }
    @PostMapping("/add")
    public String addTodo(@ModelAttribute Todo todo) {
        todoRepository.save(todo);
        return "redirect:/";
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid todo id: " + id));

        model.addAttribute("todo", todo);
        model.addAttribute("newTodo", new Todo());
        return "todo/edit";
    }
    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable("id") Long id, @ModelAttribute Todo todo, Model model) {
        // 기존의 Todo를 찾습니다.
        Todo existingTodo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid todo id: " + id));

        // 새로운 정보로 업데이트합니다.
        existingTodo.setTitle(todo.getTitle());
        existingTodo.setCompleted(todo.isCompleted());

        // 업데이트된 Todo를 저장합니다.
        todoRepository.save(existingTodo);

        // 홈 페이지로 리다이렉트합니다.
        return "redirect:/";
    }
    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable("id") Long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }
}
