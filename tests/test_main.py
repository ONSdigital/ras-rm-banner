import unittest
from unittest.mock import patch, MagicMock
from banner.routes import get_banner, get_a_banner, app
from fastapi.testclient import TestClient

client = TestClient(app)


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
        with patch('banner.routes.datastore_client') as client_mock:
            query_mock = MagicMock()
            client_mock.query.return_value = query_mock
            query_mock.fetch.return_value = [TestDataStore.Banners]
            actual = get_banner()
            self.assertEqual([self.Banners], actual)

    def test_get_a_banner(self):
        with patch('banner.routes.datastore_client') as client_mock:
            query_mock = MagicMock()
            client_mock.query.return_value = query_mock
            query_mock.fetch.return_value = [TestDataStore.Banners[0]["title"]]
            actual = get_a_banner('SDX is not working')
            self.assertEqual('SDX is not working', actual)
