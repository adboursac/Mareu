package com.lamzone.mareu.data.di;

import com.lamzone.mareu.data.service.DummyMeetingService;
import com.lamzone.mareu.data.service.MeetingApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {
    public static final MeetingApiService service = new DummyMeetingService();
    /**
     * Get instance of @{@link MeetingApiService}
     * @return
     */
    public static MeetingApiService getMeetingApiService() { return service; }

    /**
     * Get always a new instance of @{@link MeetingApiService}. Useful for tests.
     * @return
     */
    public static MeetingApiService getNewInstanceApiService() {
        return new DummyMeetingService();
    }

}
