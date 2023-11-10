package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
@AllArgsConstructor
public class LabelController {
    public static final  String LABEL_CONTROLLER_PATH = "/labels";
    public static final  String ID = "/{id}";

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    LabelService labelService;

    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label found",
            content = @Content(schema = @Schema(implementation = Label.class))),
        @ApiResponse(responseCode = "404", description = "Label not found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    })
    @GetMapping(ID)
    @ResponseStatus(OK)
    public Label show(@PathVariable Long id) {
        var task = labelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return task;
    }


    @Operation(summary = "Get all labels")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Labels found",
            content = @Content(schema = @Schema(implementation = Label.class))),
        @ApiResponse(responseCode = "404", description = "Labels not found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    })
    @GetMapping
    @ResponseStatus(OK)
    public List<Label> index() {
        return labelRepository.findAll();
    }


    @Operation(summary = "Create a new label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Label created",
            content = @Content(schema = @Schema(implementation = Label.class))),
        @ApiResponse(responseCode = "422", description = "Cannot create label with this data",
            content = @Content(schema = @Schema(implementation = Label.class)))
    })
    @PostMapping
    @ResponseStatus(CREATED)
    public Label create(@RequestBody @Valid LabelDto labelDto) {
        return labelService.create(labelDto);
    }


    @Operation(summary = "Update label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label updated",
            content = @Content(schema = @Schema(implementation = Label.class))),
        @ApiResponse(responseCode = "422", description = "Cannot update label with this data",
            content = @Content(schema = @Schema(implementation = Label.class))),
        @ApiResponse(responseCode = "404", description = "Label not found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    })
    @PutMapping(ID)
    @ResponseStatus(OK)
    public Label update(@PathVariable Long id, @RequestBody @Valid LabelDto mayBeLabelDto) {
        var label = labelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + " not found"));
        var labelUpdated = labelService.update(mayBeLabelDto, label);
        labelRepository.save(labelUpdated);
        return labelUpdated;
    }

    
    @Operation(summary = "Delete label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label deleted",
            content = @Content(schema = @Schema(implementation = Label.class))),
        @ApiResponse(responseCode = "404", description = "Label not found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    })
    @DeleteMapping(ID)
    @ResponseStatus(OK)
    public void delete(@PathVariable Long id) {
        labelRepository.deleteById(id);
    }

}
