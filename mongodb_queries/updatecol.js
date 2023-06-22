db.getCollection("updatecol").find({})
db.updatecol.deleteMany({});

db.updatecol.insertMany([
  {
    id: "1",
    type: "seen",
    catId: "cat1",
    username: "@bobby",
    datetime: ISODate("2023-01-01T00:00:00Z"),
    location: "Block 684B",
    comments: "hes playing with jake",
    photos: ["https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-feedupdate.jpg", "https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-profile.jpg"]
    
  },
  {
    id: "2",
    type: "feed",
    catId: "cat2",
    username: "@nadia",
    datetime: ISODate("2023-06-02T00:00:00Z"),
    location: "Block 555",
    comments: "eating well",
    photos: ["https://catlas-bucket.sgp1.digitaloceanspaces.com/cat2-feedingupdate.jpg"]
  },
  {
    id: "3",
    type: "seen",
    catId: "cat2",
    username: "jane_smith",
    datetime: ISODate("2022-06-03T00:00:00Z"),
    location: "mamashop",
    comments: "saw him chasing another cat",
    photos: []
  }
])
