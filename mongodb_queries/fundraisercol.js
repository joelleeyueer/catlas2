db.fundraisercol.find({})
db.fundraisercol.deleteMany({})
db.fundraisercol.insertOne({
    "fundId":"fund1",
    "approved": "approved",
    "active": true,
    "catId": "cat1",
    "username": "@user1",
    "photoUrl": "https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-profile.jpg",
    "title": "Surgery costs of Fluffy",
    "description": "Help us raise funds for Fluffy's surgery. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
    "donationGoal": 5000,
    "deadline": ISODate("2023-12-31T00:00:00Z"),
    "donations": [
        {
            "username": "@donor1",
            "amount": 100,
            "donationDate": ISODate("2023-06-24T10:00:00Z")
        },
        {
            "username": "@donor2",
            "amount": 50,
            "donationDate": ISODate("2023-06-25T10:00:00Z")
        }
    ]
})

db.fundraisercol.insertOne({
    "fundId":"fund2",
    "approved": "approved",
    "active": true,
    "catId": "cat2",
    "username": "@user2",
    "photoUrl": "https://catlas-bucket.sgp1.digitaloceanspaces.com/cat2-fundraiser.jpg",
    "title": "Surgery costs of Whiskers",
    "description": "Help us raise funds for Whiskers' surgery. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
    "donationGoal": 8500,
    "deadline": ISODate("2023-12-31T00:00:00Z"),
    "donations": [
        {
            "username": "@donor1",
            "amount": 50,
            "donationDate": ISODate("2023-06-24T10:00:00Z")
        },
        {
            "username": "@donor2",
            "amount": 25,
            "donationDate": ISODate("2023-06-25T10:00:00Z")
        }
    ]
})
