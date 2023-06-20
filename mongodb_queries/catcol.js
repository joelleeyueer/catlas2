use catDb


var cats = [
  {
    "name": "Sophie",
    "feedingnotes": "Enjoys interactive feeding toys.",
    "frequentLocations": [
      {"lat": 1.3527, "long": 103.7178}
    ]
  },
  {
    "name": "Max",
    "feedingnotes": "Needs a special diet.",
    "frequentLocations": [
      {"lat": 1.3540, "long": 103.7192}
    ]
  },
  {
    "name": "Luna",
    "feedingnotes": "Prefers wet food.",
    "frequentLocations": [
      {"lat": 1.3539, "long": 103.7187}
    ]
  },
  {
    "name": "Charlie",
    "feedingnotes": "Likes treats as rewards.",
    "frequentLocations": [
      {"lat": 1.3534, "long": 103.7199}
    ]
  },
  {
    "name": "Bella",
    "feedingnotes": "Needs a quiet feeding area.",
    "frequentLocations": [
      {"lat": 1.3543, "long": 103.7186}
    ]
  },
  {
    "name": "Oliver",
    "feedingnotes": "Requires medication with meals.",
    "frequentLocations": [
      {"lat": 1.3529, "long": 103.7195}
    ]
  }
]

db.catcol.insertMany(cats);

db.catcol.find()

db.catcol.find().forEach(function(doc) {
  doc.birthday = new Date(doc.birthday * 1000); // Convert seconds to milliseconds
  db.catcol.save(doc);
});


db.catcol.deleteMany({});

db.createCollection("locationcol")

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



db.catcol.getIndexes()

db.catcol.insertMany([
  {
    "catId": "cat1",
    "name": "Fluffy",
    "birthday": new Date("2019-05-01"),
    "sterilization": true,
    "personalityTraits": ["Playful", "Friendly"],
    "dietLikes": ["Tuna", "Chicken"],
    "dietDislikes": ["Broccoli"],
    "feedingNotes": ["Feeds twice a day"]
  },
  {
    "catId": "cat2",
    "name": "Whiskers",
    "birthday": new Date("2018-10-15"),
    "sterilization": false,
    "personalityTraits": ["Lazy", "Independent"],
    "dietLikes": ["Salmon", "Beef"],
    "dietDislikes": ["Carrots"],
    "feedingNotes": ["Feed in the morning"]
  },
  {
    "catId": "cat3",
    "name": "Mittens",
    "birthday": new Date("2020-02-28"),
    "sterilization": true,
    "personalityTraits": ["Curious", "Affectionate"],
    "dietLikes": ["Shrimp", "Lamb"],
    "dietDislikes": ["Spinach"],
    "feedingNotes": ["Feed in the evening"]
  },
  {
    "catId": "cat4",
    "name": "Socks",
    "birthday": new Date("2019-09-10"),
    "sterilization": true,
    "personalityTraits": ["Energetic", "Social"],
    "dietLikes": ["Fish", "Turkey"],
    "dietDislikes": ["Peas"],
    "feedingNotes": ["Feed throughout the day"]
  },
  {
    "catId": "cat5",
    "name": "Oreo",
    "birthday": new Date("2017-12-25"),
    "sterilization": false,
    "personalityTraits": ["Calm", "Playful"],
    "dietLikes": ["Duck", "Venison"],
    "dietDislikes": ["Cabbage"],
    "feedingNotes": ["Feed at night"]
  },
  {
    "catId": "cat6",
    "name": "Simba",
    "birthday": new Date("2018-07-03"),
    "sterilization": true,
    "personalityTraits": ["Adventurous", "Loving"],
    "dietLikes": ["Beef", "Pork"],
    "dietDislikes": ["Potato"],
    "feedingNotes": ["Feed in the morning and evening"]
  }
]);
