var elasticsearch = require('elasticsearch');
var fs = require('fs');
var indexName = 'trending'
var client = new elasticsearch.Client({
    host: 'search-esmaster-j3jbqwpyntqvepji3ddkljslkm.us-west-1.es.amazonaws.com',
    log: 'info'
});

client.ping({
    // ping usually has a 3000ms timeout
    requestTimeout: 5000
}, function(error) {
    if (error) {
        console.trace('elasticsearch cluster is down!');
    } else {
        console.log('All is well');
    }
});

// Will update existing documents found via id
function loadDataSet() {
    fs.readFile("../trendingvideos.json", {
        encoding: 'utf-8'
    }, function(err, data) {
        if (!err) {
            var items = JSON.parse(data);
            for (var i = 0; i < items.length; i++) {
                client.index({
                    index: indexName,
                    id: items[i].id,
                    type: 'video',
                    body: {
                        title: items[i].title,
                        author: items[i].author,
                        views: items[i].views,
                        date: items[i].date,
                        category: items[i].category,
                        description: items[i].description
                    }
                }, function(error, response) {
                    console.log("put item successfully.")
                })
            }
        } else {
            console.log(err);
        }
    });
}

function makeIndex() {
    client.indices.create({
        index: indexName,
    });
}

function deleteIndex() {
    client.indices.delete({
        index: indexName,
    });
}

function initMapping() {
    return client.indices.putMapping({
        index: indexName,
        type: "video",
        body: {
            properties: {
                title: {
                    type: "string"
                },
                author: {
                    type: "string"
                },
                views: {
                    type: "integer"
                },
                date: {
                    type: "date",
                    format: "basic_date"
                },
                category: {
                    type: "keyword"
                },
                description: {
                    type: "string"
                }
            }
        }
    });
}

//deleteIndex();
//makeIndex();
//initMapping();
loadDataSet();
