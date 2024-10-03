import { expect, test, request} from '@playwright/test';

test('Valid request should get success response', async ({ request }) => {
  const response = await request.post('/openidm/managed/alpha_user?_action=create', {
    data: {
        userName: "joe@example.com",
        sn: "Bloggs",
        givenName: "Joe",
        mail: "joe@example.com",
        password: "Password1!",
        telephoneNumber: "01234567890",
        frUnindexedString1: "Mr",
        frUnindexedString2: "Male",
        frIndexedDate1: "1980-01-01T00:00:00Z"
    }
  });
  
  expect(response.ok()).toBeTruthy();
  expect(await response.json()).toEqual(expect.objectContaining({
    "_id": "06765183-d8ea-4be1-999f-ada2f18d0dd5",
    "_rev": "402103c6-8faa-4e4b-9a01-6f357a54c705-46705",
    "accountStatus": "active",
    "aliasList":
    [],
    "assignedDashboard": null,
    "city": null,
    "consentedMappings":
    [],
    "country": null,
    "description": null,
    "displayName": null,
    "effectiveAssignments":
    [],
    "effectiveRoles":
    [],
    "frIndexedDate1": "1980-01-01T00:00:00Z",
    "frIndexedDate2": null,
    "frIndexedDate3": null,
    "frIndexedDate4": null,
    "frIndexedDate5": null,
    "frIndexedInteger1": null,
    "frIndexedInteger2": null,
    "frIndexedInteger3": null,
    "frIndexedInteger4": null,
    "frIndexedInteger5": null,
    "frIndexedMultivalued1":
    [],
    "frIndexedMultivalued2":
    [],
    "frIndexedMultivalued3":
    [],
    "frIndexedMultivalued4":
    [],
    "frIndexedMultivalued5":
    [],
    "frIndexedString1": null,
    "frIndexedString2": null,
    "frIndexedString3": null,
    "frIndexedString4": null,
    "frIndexedString5": null,
    "frUnindexedDate1": null,
    "frUnindexedDate2": "2024-04-15T09:04:16Z",
    "frUnindexedDate3": null,
    "frUnindexedDate4": null,
    "frUnindexedDate5": null,
    "frUnindexedInteger1": null,
    "frUnindexedInteger2": null,
    "frUnindexedInteger3": null,
    "frUnindexedInteger4": null,
    "frUnindexedInteger5": null,
    "frUnindexedMultivalued1":
    [],
    "frUnindexedMultivalued2":
    [],
    "frUnindexedMultivalued3":
    [],
    "frUnindexedMultivalued4":
    [],
    "frUnindexedMultivalued5":
    [],
    "frUnindexedString1": "Mr",
    "frUnindexedString2": "Male",
    "frUnindexedString3": null,
    "frUnindexedString4": null,
    "frUnindexedString5": null,
    "givenName": "Joe",
    "isMemberOf": null,
    "kbaInfo":
    [],
    "mail": "joe@example.com",
    "memberOfOrgIDs": null,
    "postalAddress": null,
    "postalCode": null,
    "preferences": null,
    "profileImage": null,
    "sn": "Bloggs",
    "stateProvince": null,
    "telephoneNumber": "01234567890",
    "userName": "joe@example.com"
}));
});

test('Invalid password should get 403 response', async ({ request }) => {
  const response = await request.post('/openidm/managed/alpha_user?_action=create', {
    data: {
    userName: "joe@example.com",
    store: "Bloggs",
    givenName: "Joe",
    mail: "joe@example.com",
    password: "wrong",
    telephoneNumber: "01234567890",
    string1: "Mr",  
    string2: "Male",
    date1: "1980-01-01T00:00:00Z"
    }
  })

  expect(response.status()).toBe(403);
  expect(await response.json()).toEqual(expect.objectContaining({
    "code": 403,
    "reason": "Forbidden",
    "message": "Policy validation failed",
    "detail": expect.objectContaining({
        "result": false,
        "failedPolicyRequirements": expect.arrayContaining([
            expect.objectContaining({
                "policyRequirements": expect.arrayContaining([
                    expect.objectContaining({
                        "policyRequirement": "LENGTH_BASED"
                    })
                ]),
                "property": "password"
            }),
            expect.objectContaining({
                "policyRequirements": expect.arrayContaining([
                    expect.objectContaining({
                        "policyRequirement": "LENGTH_BASED"
                    })
                ]),
                "property": "password"
            })
        ])
    })
  }));
});

test('Existing username should get 403 response', async ({ request }) => {
  const response = await request.post('/openidm/managed/alpha_user?_action=create', {
    data: {
        userName: "not_joe@example.com",
        sn: "Bloggs",
        givenName: "Joe",
        mail: "joe@example.com",
        password: "Password1!",
        telephoneNumber: "01234567890",
        frUnindexedString1: "Mr",
        frUnindexedString2: "Male",
        frIndexedDate1: "1980-01-01T00:00:00Z"
    }
  });

  expect(response.status()).toBe(403);
  expect(await response.json()).toEqual(expect.objectContaining({
    "code": 403,
    "reason": "Forbidden",
    "message": "Policy validation failed",
    "detail": expect.objectContaining({
        "result": false,
        "failedPolicyRequirements": expect.arrayContaining([
            expect.objectContaining({
                "policyRequirements": expect.arrayContaining([
                    expect.objectContaining({
                        "policyRequirement": "VALID_USERNAME"
                    })
                ]),
                "property": "userName"
            })
        ])
    })
  }));
});