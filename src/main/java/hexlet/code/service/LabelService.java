package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

public interface LabelService {
    Label create(LabelDto labelDto);
    Label update(LabelDto labelDto, Label label);

    Label getLabelById(Long id);
}
