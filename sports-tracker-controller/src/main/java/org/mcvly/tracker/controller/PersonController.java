package org.mcvly.tracker.controller;

import org.mcvly.tracker.core.Person;
import org.mcvly.tracker.core.PersonStats;
import org.mcvly.tracker.core.Training;
import org.mcvly.tracker.service.STServiceException;
import org.mcvly.tracker.service.SportTrackerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author mcvly
 * @since 12.04.14
 */
@RestController
@RequestMapping(value="/person/", produces= MediaType.APPLICATION_JSON_VALUE)
class PersonController {

    @Resource
    private SportTrackerService sportTrackerService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Person information(@PathVariable("id") Integer personId) {
        Person personInformation = sportTrackerService.getPersonInformation(personId);
        if (personInformation == null) {
            throw new ResourceNotFoundException();
        } else {
            return personInformation;
        }
    }

    @RequestMapping(value = "{id}/stats", method = RequestMethod.GET)
    public List<PersonStats> stats(@PathVariable("id") Integer personId) {
        try {
            return sportTrackerService.getPersonStats(personId);
        } catch (STServiceException e) {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "{id}/traininfo", method = RequestMethod.GET)
    public List<Training> trainingsInfo(@PathVariable("id") Integer personId,
                                        @RequestParam(value = "since", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since) {
        return sportTrackerService.getTrainingInfos(personId, since);
    }

    @RequestMapping(value = "{id}/trainings", params = { "page", "size" }, method = RequestMethod.GET)
    public List<Training> trainings(@PathVariable("id") Integer personId,
                                    @RequestParam(value = "page", required = false) int page,
                                    @RequestParam(value = "size", required = false) int size) {
        return sportTrackerService.getTrainingsWithExercises(personId, page, size);
    }

}
