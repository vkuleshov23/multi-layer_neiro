package ai.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ChartUpdateEvent extends ApplicationEvent {

    private Double errorData;

    public ChartUpdateEvent(Double errorData) {
        super(errorData);
        this.errorData = errorData;
    }
}
