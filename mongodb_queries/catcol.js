use catDb

db.catcol.find()

db.catcol.find().forEach(function(doc) {
  doc.birthday = new Date(doc.birthday * 1000); // Convert seconds to milliseconds
  db.catcol.save(doc);
});


db.catcol.deleteMany({});
db.catcol.deleteMany({
  catId: {
    $in: ["cat5", "cat6"]
  }
})


db.createCollection("locationcol")

db.createCollection("updatecol")


db.catcol.find().forEach(function(cat) {
  cat.frequentLocationList.forEach(function(location) {
    db.locationcol.insert({
      id: cat.id,
      catId: cat.catId,
      location: location
    });
  });

db.catcol.updateMany({}, { $unset: { frequentLocationList: "" } })
});


db.catcol.createIndex({"frequentLocationList.$**": "2dsphere"})

db.catcol.insertOne(
  {
    "catId": "cat5",
    "profilePhoto":"https://catlas-bucket.sgp1.digitaloceanspaces.com/cat5-profile.jpg",
    "name": "Mika",
    "birthday": new Date("2022-05-01"),
    "sterilization": true,
    "personalityTraits": ["Weird", "Zoomies"],
    "dietLikes": ["Tuna", "Chicken"],
    "dietDislikes": ["Cheap food"],
    "feedingNotes": ["Irregular feeding time"]
  })



db.catcol.getIndexes()

db.catcol.insertMany([
  {
    "catId": "cat1",
    "profilePhoto":"https://catlas-bucket.sgp1.digitaloceanspaces.com/cat1-profile.jpg",
    "name": "Fluffy",
    "gender": "male",
    "birthday": new Date("2019-05-01"),
    "sterilization": true,
    "personalityTraits": ["Playful", "Friendly"],
    "dietLikes": ["Tuna", "Chicken"],
    "dietDislikes": ["Broccoli"],
    "feedingNotes": ["Feeds twice a day"]
  },
  {
    "catId": "cat2",
    "profilePhoto":"https://catlas-bucket.sgp1.digitaloceanspaces.com/cat2-profile.jpg",
    "name": "Whiskers",
    "gender": "female",
    "birthday": new Date("2018-10-15"),
    "sterilization": false,
    "personalityTraits": ["Lazy", "Independent"],
    "dietLikes": ["Salmon", "Beef"],
    "dietDislikes": ["Carrots"],
    "feedingNotes": ["Feed in the morning"]
  },
  {
    "catId": "cat3",
    "profilePhoto":"https://catlas-bucket.sgp1.digitaloceanspaces.com/cat3-profile.jpg",
    "name": "Mittens",
    "gender": "female",
    "birthday": new Date("2020-02-28"),
    "sterilization": true,
    "personalityTraits": ["Curious", "Affectionate"],
    "dietLikes": ["Shrimp", "Lamb"],
    "dietDislikes": ["Spinach"],
    "feedingNotes": ["Feed in the evening"]
  },
  {
    "catId": "cat4",
    "profilePhoto":"https://catlas-bucket.sgp1.digitaloceanspaces.com/cat4-profile.jpg",
    "name": "Socks",
    "gender": "male",
    "birthday": new Date("2019-09-10"),
    "sterilization": true,
    "personalityTraits": ["Energetic", "Social"],
    "dietLikes": ["Fish", "Turkey"],
    "dietDislikes": ["Peas"],
    "feedingNotes": ["Feed throughout the day"]
  },
  {
    "catId": "cat5",
    "profilePhoto":"https://catlas-bucket.sgp1.digitaloceanspaces.com/cat5-profile.jpg",
    "name": "Mika",
    "gender": "male",
    "birthday": new Date("2022-05-01"),
    "sterilization": true,
    "personalityTraits": ["Weird", "Zoomies"],
    "dietLikes": ["Tuna", "Chicken"],
    "dietDislikes": ["Cheap food"],
    "feedingNotes": ["Irregular feeding time"]
  }
]);
