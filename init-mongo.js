db.createUser(
    {
        user: "root",
        pwd: "wfit",
        roles:[
            {
                role: "readWrite",
                db:"pix"
            }
        ]
    }
);
db.createCollection("transacao_pix");