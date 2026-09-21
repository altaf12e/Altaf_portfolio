package com.portfolio.app.service;

import com.portfolio.app.model.Skill;
import com.portfolio.app.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAllByOrderByCategoryAscProficiencyDesc();
    }

    public List<Skill> getFeaturedSkills() {
        return skillRepository.findByFeaturedTrue();
    }

    public Map<String, List<Skill>> getSkillsGroupedByCategory() {
        List<Skill> all = getAllSkills();
        // Maintain a sensible category order
        Map<String, List<Skill>> grouped = new LinkedHashMap<>();
        List<String> orderedCategories = Arrays.asList("Backend", "Frontend", "Database", "DevOps & Cloud", "Core Tools");
        
        for (String cat : orderedCategories) {
            List<Skill> matches = all.stream()
                    .filter(s -> s.getCategory().equalsIgnoreCase(cat))
                    .collect(Collectors.toList());
            if (!matches.isEmpty()) {
                grouped.put(cat, matches);
            }
        }

        // Add any remaining categories
        all.stream()
                .filter(s -> orderedCategories.stream().noneMatch(c -> c.equalsIgnoreCase(s.getCategory())))
                .forEach(s -> grouped.computeIfAbsent(s.getCategory(), k -> new ArrayList<>()).add(s));

        return grouped;
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    public long getTotalSkillsCount() {
        return skillRepository.count();
    }
}

