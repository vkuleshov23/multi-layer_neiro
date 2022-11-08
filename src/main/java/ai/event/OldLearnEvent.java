package ai.event;

import ai.service.NeiroOld;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OldLearnEvent extends ApplicationEvent {

    NeiroOld neiroOld;

    public OldLearnEvent(NeiroOld neiroOld) {
        super(neiroOld);
        this.neiroOld = neiroOld;
    }
}
