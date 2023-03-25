# User

## Create User

### Request

```jsx
POST /api/v1/users

{
    "userName":"mybox-test",
    "userPassword":"password"
}
```

### Response

```jsx
200 OK

{
    "userId": "{{USER_ID}}",
    "userName": "mybox-test",
    "rootDirectory": "{{USER_ROOT_DIR_ID}}",
    "maxUsage": 53687091200,
    "currentUsage": 0
}
```

## Get User

### Request

```jsx
GET /api/v1/users/{{USER_ID}}
```

### Response

```jsx
200 OK

{
    "userId": "{{USER_ID}}",
    "userName": "mybox-test",
    "rootDirectory": "{{USER_ROOT_DIR_ID}}",
    "maxUsage": 53687091200,
    "currentUsage": 0
}
```

## Login User

### Request

```jsx
POST /login

{
    "username":"mybox-test",
    "password":"password"
}
```

### Response

```jsx
200 OK

{
    "token": "{{JWT_TOKEN}}"
}
```

# Directory

## Create Directory

### Request

```jsx
POST /api/v1/users/{{USER_ID}}/directories

Authorization: Bearer {{JWT_TOKEN}}

{
    "directoryName": "test-dir",
    "directoryOwner": "{{USER_ID}}",
    "directoryParent": "{{USER_ROOT_DIR_ID}}"
}
```

### Response

```jsx
200 OK

{
    "directoryId": "{{DIR_ID}}",
    "directoryName": "test-dir",
    "directoryOwner": "{{USER_ID}}",
    "directoryParent": "{{USER_ROOT_DIR_ID}}",
    "size": 0
}
```

## Get Directory Items

### Request

```jsx
GET /api/v1/users/{{USER_ID}}/directories/{{USER_ROOT_DIR_ID}}/items

Authorization: Bearer {{JWT_TOKEN}}
```

### Response

```jsx
200 OK

{
    "items": [
        {
            "itemType": "DIRECTORY",
            "itemId": "{{ITEM_ID}}",
            "itemName": "test-dir-child1"
        },
        {
            "itemType": "DIRECTORY",
            "itemId": "{{ITEM_ID}}",
            "itemName": "test-dir-child2"
        },
        {
            "itemType": "FILE",
            "itemId": "{{ITEM_ID}}",
            "itemName": "test-file-1"
        }
    ]
}
```

## Delete Directory

### Request

```jsx
DELETE /api/v1/users/{{USER_ID}}/directories/{{DIR_ID}}

Authorization: Bearer {{JWT_TOKEN}}
```

### Response

```jsx
200 OK
```

# File

## Upload File

### Request

```jsx
POST /api/v1/directories/{{DIR_ID}}/files

Authorization: Bearer {{JWT_TOKEN}}
User-Agent: PostmanRuntime/7.31.3
Accept: */*
Postman-Token: 491e2887-1f8a-4326-be6c-c7d030bf4f35
Host: localhost:8080
Accept-Encoding: gzip, deflate, br
Connection: keep-alive
Content-Type: multipart/form-data; boundary=--------------------------835825100299529514182067
Content-Length: 11905911

{
    filePart: undefined
    userId: "{{USER_ID}}"
    fileName: "test-file-1"
}
```

### Response

```jsx
200 OK

{
    "fileId": "{{FILE_ID}}",
    "fileName": "test-file-1",
    "fileOwnerId": "{{USER_ID}}",
    "fileParent": "{{DIR_ID}}",
    "fileSize": 11905911
}
```

## Download File

### Request

```jsx
GET /api/v1/directories/{{DIR_ID}}/files/{{FILE_ID}}

Authorization: Bearer {{JWT_TOKEN}}

```

### Response

```jsx
200 OK
```

## Delete File

### Request

```jsx
DELETE /api/v1/files/{{FILE_ID}}

Authorization: Bearer {{JWT_TOKEN}}
```

### Response

```jsx
200 OK
```
