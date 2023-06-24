db.updatecol.find({})
db.updatecol.deleteMany({});

db.updatecol.remove({ _id: ObjectId('6496b6096ee6549f62d03380') });


db.updatecol.insert({
    id: "6",
    type: "seen",
    catId: "cat1",
    username: "@joel",
    datetime: ISODate("2023-01-04T00:00:00Z"),
    location: "Block 684B",
    comments: "sleeping again",
    photos: [
      "https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-feedupdate.jpg",
      "https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-profile.jpg"
    ]
})


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
    username: "@jane_smith",
    datetime: ISODate("2023-06-03T00:00:00Z"),
    location: "mamashop",
    comments: "saw him chasing another cat",
    photos: []
  },
  {
    id: "4",
    type: "feed",
    catId: "cat4",
    username: "@zendaya",
    datetime: ISODate("2023-06-22T00:00:00Z"),
    location: "near the playground",
    comments: "he ate",
    photos: []
  },
  {
    id: "1",
    type: "seen",
    catId: "cat1",
    username: "@joel",
    datetime: ISODate("2023-01-04T00:00:00Z"),
    location: "Block 684B",
    comments: "sleeping again",
    photos: ["https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-feedupdate.jpg", "https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-profile.jpg"]
    
  }
])
