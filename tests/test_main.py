import unittest


class TestDataStore(unittest.TestCase):
    Banners = [
        {
            "title": "SDX is not working",
            "value": "We are carrying out essential maintenance to the service.\
                Please continue to use the system and submit data as normal.\
                It will only affect your response status which may not display\
                as completed until the maintenance has been completed.",
            "banner_active": True
        },
        {
            "title": "Unexpected outage",
            "value": "We are currently experiencing technical difficulties.\
                Our engineers are investigating and hope to have the service\
                back up shortly.Thank you for your patience",
            "banner_active": False
        }]

    def test_get_banner_correct_response(self):
        return 'hello world'

    def test_get_a_banner(self):
        return 'hello world'
