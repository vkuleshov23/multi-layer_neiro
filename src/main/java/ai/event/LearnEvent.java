package ai.event;

import ai.service.NeiroOld;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LearnEvent extends ApplicationEvent {

    NeiroOld neiroOld;

    public LearnEvent(NeiroOld neiroOld) {
        super(neiroOld);
        this.neiroOld = neiroOld;
    }
}
