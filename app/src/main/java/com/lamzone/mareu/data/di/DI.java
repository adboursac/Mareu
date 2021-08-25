package com.lamzone.mareu.data.di;

import com.lamzone.mareu.data.service.DummyMeetingApiService;
import com.lamzone.mareu.data.service.MeetingApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static MeetingApiService service = new DummyMeetingApiService();

    /**
     * Get instance of @{@link MeetingApiService}
     * @return
     */
    public static MeetingApiService getMeetingApiService() { return service; }

    /**
     * generate a new instance of @{@link MeetingApiService}. Useful for tests.
     * @return
     */
    public static void generateNewApiService() {
        service = new DummyMeetingApiService();
    }
}
