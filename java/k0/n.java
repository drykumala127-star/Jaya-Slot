package k0;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public abstract class n {
    public static OffsetDateTime a(Instant instant, ZoneOffset zoneOffset) {
        return instant.atOffset(zoneOffset);
    }
}
