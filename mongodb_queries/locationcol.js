db.getCollection("locationcol").find({})

db.locationcol.deleteMany({});

db.locationcol.createIndex({ location: "2dsphere" })

db.locationcol.find({
  location: {
    $nearSphere: {
      $geometry: {
        type: "Point",
        coordinates: [103.719046, 1.353747] // Specify the longitude and latitude of the center point
      },
      $maxDistance: 1000 // Specify the radius in meters
    }
  }
})

db.locationcol.insertOne({
    "catId": "cat5",
    "location": { "type": "Point", "coordinates": [103.7204, 1.3525] }
})

db.locationcol.insertMany([
  {
    "catId": "cat1",
    "location": { "type": "Point", "coordinates": [103.7184, 1.3537] }
  },
  {
    "catId": "cat1",
    "location": { "type": "Point", "coordinates": [103.7185, 1.3536] }
  },
  {
    "catId": "cat1",
    "location": { "type": "Point", "coordinates": [103.7184, 1.3535] }
  },
  {
    "catId": "cat2",
    "location": { "type": "Point", "coordinates": [103.7197, 1.3539] }
  },
  {
    "catId": "cat2",
    "location": { "type": "Point", "coordinates": [103.7198, 1.3538] }
  },
  {
    "catId": "cat2",
    "location": { "type": "Point", "coordinates": [103.7197, 1.3537] }
  },
  {
    "catId": "cat3",
    "location": { "type": "Point", "coordinates": [103.7048, 1.3446] }
  },
  {
    "catId": "cat3",
    "location": { "type": "Point", "coordinates": [103.7049, 1.3447] }
  },
  {
    "catId": "cat3",
    "location": { "type": "Point", "coordinates": [103.7050, 1.3446] }
  },
  {
    "catId": "cat4",
    "location": { "type": "Point", "coordinates": [103.7046, 1.3418] }
  },
  {
    "catId": "cat4",
    "location": { "type": "Point", "coordinates": [103.7047, 1.3419] }
  },
  {
    "catId": "cat4",
    "location": { "type": "Point", "coordinates": [103.7046, 1.3420] }
  }
]);
