package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public Label create(LabelDto labelDto) {
        Label label = new Label();
        label.setName(labelDto.getName());
        labelRepository.save(label);
        return label;
    }

    public Label update(LabelDto labelDto, Label label) {
        label.setName(labelDto.getName());
        labelRepository.save(label);
        return label;
    }

    public Label getLabelById(final Long id) {
        return labelRepository.findById(id).orElseThrow();
    }
}
