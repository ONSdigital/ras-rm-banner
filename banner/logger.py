import logging
import os

from structlog import configure, wrap_logger
from structlog.processors import format_exc_info, JSONRenderer, TimeStamper
from structlog.stdlib import add_log_level, filter_by_level

logger_date_format = os.getenv('LOGGING_DATE_FORMAT', "%Y-%m-%dT%H:%M%s")
# 20 means info level of severity
log_level = os.getenv('LOGGING_LEVEL', 20)


# Add the severity level to the event dict.
def add_severity_level(logger, method_name, event_dict):  # pylint: disable=unused-argument

    # as we are extending structlog processor, it passes three attributes by default
    if method_name == "warn":
        # The stdlib has an alias warn for warning
        # this method name is not understood by logging api
        # hence changing to warning
        method_name = "warning"
    event_dict["severity"] = method_name
    return event_dict


# for basic configurations, calling basicConfig() to configure the root logger
# stdout is the destination to log the message.
# logging.basicConfig(stream=sys.stdout, level=log_level, format="%(name)s %(message)s")
logging.basicConfig(filename='logs.log', level=log_level, format="%(name)s %(message)s")
configure(processors=[add_severity_level, add_log_level, filter_by_level, format_exc_info,
                      TimeStamper(fmt=logger_date_format,
                                  utc=True, key="created_at"),
                      JSONRenderer(indent=None)])

# module-level logger si it's obvious where events are logged just from the logger name.
logger = wrap_logger(logging.getLogger(__name__))
logger.info("Logging started")
