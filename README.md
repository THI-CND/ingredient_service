# IngredientService
Der IngredientService verwaltet Zutaten für Rezepte.

## APIs
### REST
#### GET /ingredients
Gibt alle Zutaten zurück.

**Response:**
- Status: `200 OK`
    ```json
    [
      {
        "id": 1,
        "name": "Mehl",
        "unit": "g",
        "tags": ["vegetarisch", "vegan"]
      },
      ...
    ]
    ```

#### GET /ingredients/{id}
Gibt die Zutat mit der ID {id} zurück.

**Response:**
- Status: `200 OK`
    ```json
    {
      "id": 1,
      "name": "Mehl",
      "unit": "g",
      "tags": ["vegetarisch", "vegan"]
    }
    ```
- Status: `404 Not Found`

#### POST /ingredients
Erstellt eine neue Zutat.

**Request Body:**

```json
{
  "name": "Mehl",
  "unit": "g",    
  "tags": ["vegetarisch", "vegan"]
}
```
**Response:**
- Status: `201 Created`
    ```json
    {
      "id": 1,
      "name": "Mehl",
      "unit": "g",
      "tags": ["vegetarisch", "vegan"]
    }
    ```
- Status: `400 Bad Request`: Name existiert bereits.

#### PUT /ingredients/{id}
Aktualisiert die Zutat mit der ID {id}.

**Request Body:**

```json
{
  "name": "Mehl",
  "unit": "g",    
  "tags": ["vegetarisch", "vegan"]
}
```

**Response:**
- Status: `200 OK`
    ```json
    {
      "id": 1,
      "name": "Mehl",
      "unit": "g",
      "tags": ["vegetarisch", "vegan"]
    }
    ```
- Status: `404 Not Found`

#### DELETE /ingredients/{id}
Löscht die Zutat mit der ID {id}.

**Response:**
- Status: `204 No Content`
- Status: `404 Not Found`

### gRPC
```protobuf
syntax = "proto3";

package de.thi.cnd.ingredientservice;

option java_multiple_files = true;
option java_package = "de.thi.cnd.ingredient";
option java_outer_classname = "IngredientProto";

service IngredientService {
  rpc GetIngredients(Empty) returns (IngredientsResponse);
  rpc GetIngredient(IngredientIdRequest) returns (IngredientResponse);
  rpc CreateIngredient(CreateIngredientRequest) returns (IngredientResponse);
  rpc UpdateIngredient(UpdateIngredientRequest) returns (IngredientResponse);
  rpc DeleteIngredient(IngredientIdRequest) returns (Empty);
  rpc GetTags(Empty) returns (TagsResponse);
}

message Empty {
}

message IngredientsResponse {
  repeated IngredientResponse ingredients = 1;
}

message IngredientResponse {
  int64 id = 1;
  string name = 2;
  string unit = 3;
  repeated string tags = 4;
}

message IngredientIdRequest {
  int64 id = 1;
}

message CreateIngredientRequest {
  string name = 1;
  string unit = 2;
  repeated string tags = 3;
}

message UpdateIngredientRequest {
  int64 id = 1;
  string name = 2;
  string unit = 3;
  repeated string tags = 4;
}

message TagsResponse {
  repeated string tags = 1;
}
```

## Events
### ìngredient.created
Wird gesendet, wenn eine neue Zutat erstellt wurde.

**Payload:**
```json
{
  "id": 1,
  "name": "Mehl",
  "unit": "g",
  "tags": ["vegetarisch", "vegan"]
}
```

### ingredient.updated
Wird gesendet, wenn eine Zutat aktualisiert wurde.

**Payload:**
```json
{
  "id": 1,
  "name": "Mehl",
  "unit": "g",
  "tags": ["vegetarisch", "vegan"]
}
```

### ingredient.deleted
Wird gesendet, wenn eine Zutat gelöscht wurde.

**Payload:**
```json
{
  "id": 1,
  "name": "Mehl",
  "unit": "g",
  "tags": ["vegetarisch", "vegan"]
}
```

### ingredient.tags.created
Wird gesendet, wenn ein neuer Tag erstellt wurde.

**Payload:**
```json
{
  "name": "vegetarisch"
}
```

