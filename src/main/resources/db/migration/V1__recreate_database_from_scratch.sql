DROP TABLE IF EXISTS [Identity].LoginAttempts ;
DROP TABLE IF EXISTS [Identity].UserRoles ;
DROP TABLE IF EXISTS [Identity].UserClaims ;
DROP TABLE IF EXISTS [Identity].UserLogins ;
DROP TABLE IF EXISTS [Identity].UserTokens ;
DROP TABLE IF EXISTS [Identity].RoleClaims ;
DROP TABLE IF EXISTS [Identity].Ticket ;
DROP TABLE IF EXISTS [Identity].[Contract] ;
DROP TABLE IF EXISTS [Identity].ContractType ;
DROP TABLE iF EXISTS [Identity].Bijlages ;
DROP TABLE IF EXISTS [Identity].[Role] ;
DROP TABLE IF EXISTS [Identity].Clients ;
DROP TABLE IF EXISTS [Identity].Employees ;
DROP TABLE IF EXISTS [Identity].Companies ;
DROP TABLE IF EXISTS [Identity].[User] ;

CREATE TABLE [Identity].[Bijlages] (
    [BijlageId] nvarchar(450) NOT NULL,
    [FileType] nvarchar(max) NULL,
    [Name] nvarchar(max) NULL,
    [DataFiles] varbinary(max) NULL,
    CONSTRAINT [PK_Bijlages] PRIMARY KEY ([BijlageId])
    );

GO

CREATE TABLE [Identity].[Companies] (
    [CompanyId] nvarchar(450) NOT NULL,
    [Name] nvarchar(max) NULL,
    [Adress] nvarchar(max) NULL,
    [PhoneNumber] nvarchar(max) NULL,
    CONSTRAINT [PK_Companies] PRIMARY KEY ([CompanyId])
    );

GO

CREATE TABLE [Identity].[ContractType] (
    [ContractTypeId] int NOT NULL IDENTITY,
    [Naam] nvarchar(100) NOT NULL,
    [ManierAanmakenTicket] int NOT NULL,
    [GedekteTijdstippen] int NOT NULL,
    [MaximaleAfhandeltijd] float NOT NULL,
    [MinimaleDoorlooptijd] float NOT NULL,
    [Prijs] float NOT NULL,
    [Status] int NOT NULL,
    CONSTRAINT [PK_ContractType] PRIMARY KEY ([ContractTypeId])
    );

GO

CREATE TABLE [Identity].[LoginAttempts] (
    [LoginAttemptId] nvarchar(450) NOT NULL,
    [AttemptTimeStamp] datetime2 NOT NULL,
    [UserName] nvarchar(max) NULL,
    [Succeeded] bit NOT NULL,
    CONSTRAINT [PK_LoginAttempts] PRIMARY KEY ([LoginAttemptId])
    );

GO

CREATE TABLE [Identity].[Role] (
    [Id] nvarchar(450) NOT NULL,
    [Name] nvarchar(256) NULL,
    [NormalizedName] nvarchar(256) NULL,
    [ConcurrencyStamp] nvarchar(max) NULL,
    CONSTRAINT [PK_Role] PRIMARY KEY ([Id])
    );

GO

CREATE TABLE [Identity].[User] (
    [Id] nvarchar(450) NOT NULL,
    [UserName] nvarchar(256) NULL,
    [NormalizedUserName] nvarchar(256) NULL,
    [Email] nvarchar(256) NULL,
    [NormalizedEmail] nvarchar(256) NULL,
    [EmailConfirmed] bit NOT NULL,
    [PasswordHash] nvarchar(max) NULL,
    [PasswordHashForJava] nvarchar(max) NULL,
    [SecurityStamp] nvarchar(max) NULL,
    [ConcurrencyStamp] nvarchar(max) NULL,
    [PhoneNumber] nvarchar(max) NULL,
    [PhoneNumberConfirmed] bit NOT NULL,
    [TwoFactorEnabled] bit NOT NULL,
    [LockoutEnd] datetimeoffset NULL,
    [LockoutEnabled] bit NOT NULL,
    [AccessFailedCount] int NOT NULL,
    [FirstName] nvarchar(max) NULL,
    [LastName] nvarchar(max) NULL,
    [LastSession] datetime2 NOT NULL,
	[StatusGebruiker] nvarchar(max) NULL,
    CONSTRAINT [PK_User] PRIMARY KEY ([Id])
    );

GO

CREATE TABLE [Identity].[Contract] (
    [ContractId] int NOT NULL IDENTITY,
    [TypeContractTypeId] int NOT NULL,
    [Status] int NOT NULL,
    [StartDatum] datetime2 NOT NULL,
    [EindDatum] datetime2 NOT NULL,
    [CompanyId] nvarchar(450) NULL,
    CONSTRAINT [PK_Contract] PRIMARY KEY ([ContractId]),
    CONSTRAINT [FK_Contract_Companies_CompanyId] FOREIGN KEY ([CompanyId]) REFERENCES [Identity].[Companies] ([CompanyId]) ON DELETE NO ACTION,
    CONSTRAINT [FK_Contract_ContractType_TypeContractTypeId] FOREIGN KEY ([TypeContractTypeId]) REFERENCES [Identity].[ContractType] ([ContractTypeId]) ON DELETE NO ACTION
    );

GO

CREATE TABLE [Identity].[RoleClaims] (
    [Id] int NOT NULL IDENTITY,
    [RoleId] nvarchar(450) NOT NULL,
    [ClaimType] nvarchar(max) NULL,
    [ClaimValue] nvarchar(max) NULL,
    CONSTRAINT [PK_RoleClaims] PRIMARY KEY ([Id]),
    CONSTRAINT [FK_RoleClaims_Role_RoleId] FOREIGN KEY ([RoleId]) REFERENCES [Identity].[Role] ([Id]) ON DELETE CASCADE
    );

GO

CREATE TABLE [Identity].[Clients] (
    [ClientId] nvarchar(450) NOT NULL,
    [ApplicationUserId] nvarchar(450) NULL,
    [CompanyId] nvarchar(450) NULL,
    CONSTRAINT [PK_Clients] PRIMARY KEY ([ClientId]),
    CONSTRAINT [FK_Clients_User_ApplicationUserId] FOREIGN KEY ([ApplicationUserId]) REFERENCES [Identity].[User] ([Id]) ON DELETE NO ACTION,
    CONSTRAINT [FK_Clients_Companies_CompanyId] FOREIGN KEY ([CompanyId]) REFERENCES [Identity].[Companies] ([CompanyId]) ON DELETE NO ACTION
    );

GO

CREATE TABLE [Identity].[Employees] (
    [EmployeeId] nvarchar(450) NOT NULL,
    [ApplicationUserId] nvarchar(450) NULL,
    CONSTRAINT [PK_Employees] PRIMARY KEY ([EmployeeId]),
    CONSTRAINT [FK_Employees_User_ApplicationUserId] FOREIGN KEY ([ApplicationUserId]) REFERENCES [Identity].[User] ([Id]) ON DELETE NO ACTION
    );

GO

CREATE TABLE [Identity].[UserClaims] (
    [Id] int NOT NULL IDENTITY,
    [UserId] nvarchar(450) NOT NULL,
    [ClaimType] nvarchar(max) NULL,
    [ClaimValue] nvarchar(max) NULL,
    CONSTRAINT [PK_UserClaims] PRIMARY KEY ([Id]),
    CONSTRAINT [FK_UserClaims_User_UserId] FOREIGN KEY ([UserId]) REFERENCES [Identity].[User] ([Id]) ON DELETE CASCADE
    );

GO

CREATE TABLE [Identity].[UserLogins] (
    [LoginProvider] nvarchar(450) NOT NULL,
    [ProviderKey] nvarchar(450) NOT NULL,
    [ProviderDisplayName] nvarchar(max) NULL,
    [UserId] nvarchar(450) NOT NULL,
    CONSTRAINT [PK_UserLogins] PRIMARY KEY ([LoginProvider], [ProviderKey]),
    CONSTRAINT [FK_UserLogins_User_UserId] FOREIGN KEY ([UserId]) REFERENCES [Identity].[User] ([Id]) ON DELETE CASCADE
    );

GO

CREATE TABLE [Identity].[UserRoles] (
    [UserId] nvarchar(450) NOT NULL,
    [RoleId] nvarchar(450) NOT NULL,
    CONSTRAINT [PK_UserRoles] PRIMARY KEY ([UserId], [RoleId]),
    CONSTRAINT [FK_UserRoles_Role_RoleId] FOREIGN KEY ([RoleId]) REFERENCES [Identity].[Role] ([Id]) ON DELETE CASCADE,
    CONSTRAINT [FK_UserRoles_User_UserId] FOREIGN KEY ([UserId]) REFERENCES [Identity].[User] ([Id]) ON DELETE CASCADE
    );

GO

CREATE TABLE [Identity].[UserTokens] (
    [UserId] nvarchar(450) NOT NULL,
    [LoginProvider] nvarchar(450) NOT NULL,
    [Name] nvarchar(450) NOT NULL,
    [Value] nvarchar(max) NULL,
    CONSTRAINT [PK_UserTokens] PRIMARY KEY ([UserId], [LoginProvider], [Name]),
    CONSTRAINT [FK_UserTokens_User_UserId] FOREIGN KEY ([UserId]) REFERENCES [Identity].[User] ([Id]) ON DELETE CASCADE
    );

GO

CREATE TABLE [Identity].[Ticket] (
    [TicketId] int NOT NULL IDENTITY,
    [Titel] nvarchar(100) NOT NULL,
    [Omschrijving] nvarchar(max) NOT NULL,
    [Opmerkingen] nvarchar(max) NULL,
    [IsGewijzigd] bit NULL,
    [DatumAangemaakt] datetime2 NOT NULL,
    [DatumAfgesloten] datetime2 NULL,
    [DatumGewijzigd] datetime2 NOT NULL,
    [Type] int NOT NULL,
    [Status] int NOT NULL,
    [AantalWijzigingen] int NOT NULL,
    [CompanyId] nvarchar(450) NULL,
    [EmployeeId] nvarchar(450) NULL,
    [BijlageId] nvarchar(450) NULL,
    [ContractId] int NULL,
    [ImageDescription] nvarchar(max) NULL,
    CONSTRAINT [PK_Ticket] PRIMARY KEY ([TicketId]),
    CONSTRAINT [FK_Ticket_Bijlages_BijlageId] FOREIGN KEY ([BijlageId]) REFERENCES [Identity].[Bijlages] ([BijlageId]) ON DELETE NO ACTION,
    CONSTRAINT [FK_Ticket_Companies_CompanyId] FOREIGN KEY ([CompanyId]) REFERENCES [Identity].[Companies] ([CompanyId]) ON DELETE NO ACTION,
    CONSTRAINT [FK_Ticket_Contract_ContractId] FOREIGN KEY ([ContractId]) REFERENCES [Identity].[Contract] ([ContractId]) ON DELETE NO ACTION,
    CONSTRAINT [FK_Ticket_Employees_EmployeeId] FOREIGN KEY ([EmployeeId]) REFERENCES [Identity].[Employees] ([EmployeeId]) ON DELETE NO ACTION
    );

GO

CREATE UNIQUE INDEX [IX_Clients_ApplicationUserId] ON [Identity].[Clients] ([ApplicationUserId]) WHERE [ApplicationUserId] IS NOT NULL;

GO

CREATE INDEX [IX_Clients_CompanyId] ON [Identity].[Clients] ([CompanyId]);

GO

CREATE INDEX [IX_Contract_CompanyId] ON [Identity].[Contract] ([CompanyId]);

GO

CREATE INDEX [IX_Contract_TypeContractTypeId] ON [Identity].[Contract] ([TypeContractTypeId]);

GO

CREATE UNIQUE INDEX [IX_Employees_ApplicationUserId] ON [Identity].[Employees] ([ApplicationUserId]) WHERE [ApplicationUserId] IS NOT NULL;

GO

CREATE UNIQUE INDEX [RoleNameIndex] ON [Identity].[Role] ([NormalizedName]) WHERE [NormalizedName] IS NOT NULL;

GO

CREATE INDEX [IX_RoleClaims_RoleId] ON [Identity].[RoleClaims] ([RoleId]);

GO

CREATE INDEX [IX_Ticket_BijlageId] ON [Identity].[Ticket] ([BijlageId]);

GO

CREATE INDEX [IX_Ticket_CompanyId] ON [Identity].[Ticket] ([CompanyId]);

GO

CREATE INDEX [IX_Ticket_ContractId] ON [Identity].[Ticket] ([ContractId]);

GO

CREATE INDEX [IX_Ticket_EmployeeId] ON [Identity].[Ticket] ([EmployeeId]);

GO

CREATE INDEX [EmailIndex] ON [Identity].[User] ([NormalizedEmail]);

GO

CREATE UNIQUE INDEX [UserNameIndex] ON [Identity].[User] ([NormalizedUserName]) WHERE [NormalizedUserName] IS NOT NULL;

GO

CREATE INDEX [IX_UserClaims_UserId] ON [Identity].[UserClaims] ([UserId]);

GO

CREATE INDEX [IX_UserLogins_UserId] ON [Identity].[UserLogins] ([UserId]);

GO

CREATE INDEX [IX_UserRoles_RoleId] ON [Identity].[UserRoles] ([RoleId]);

GO

DECLARE @var0 sysname;
SELECT @var0 = [d].[name]
FROM [sys].[default_constraints] [d]
    INNER JOIN [sys].[columns] [c] ON [d].[parent_column_id] = [c].[column_id] AND [d].[parent_object_id] = [c].[object_id]
WHERE ([d].[parent_object_id] = OBJECT_ID(N'[Identity].[Ticket]') AND [c].[name] = N'Type');
IF @var0 IS NOT NULL EXEC(N'ALTER TABLE [Identity].[Ticket] DROP CONSTRAINT [' + @var0 + '];');
ALTER TABLE [Identity].[Ticket] DROP COLUMN [Type];

GO

ALTER TABLE [Identity].[ContractType] ADD [aantalContracten] int NOT NULL DEFAULT 0;

GO




