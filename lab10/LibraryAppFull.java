import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibraryAppFull extends JFrame {
    private Connection conn;

    private DefaultTableModel bookModel;
    private DefaultTableModel authorModel;
    private DefaultTableModel genreModel;

    public LibraryAppFull() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/library_db",
                    "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cannot connect to DB");
            System.exit(1);
        }

        setTitle("Library App Full CRUD");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        JPanel bookPanel = new JPanel(new BorderLayout());
        bookModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Year", "Quantity", "Authors", "Genres"}, 0);
        JTable bookTable = new JTable(bookModel);
        bookPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel bookButtons = new JPanel();
        JButton addBookBtn = new JButton("Add Book");
        JButton editBookBtn = new JButton("Edit Book");
        JButton delBookBtn = new JButton("Delete Book");
        JButton refreshBookBtn = new JButton("Refresh");
        bookButtons.add(addBookBtn);
        bookButtons.add(editBookBtn);
        bookButtons.add(delBookBtn);
        bookButtons.add(refreshBookBtn);
        bookPanel.add(bookButtons, BorderLayout.SOUTH);

        tabs.addTab("Books", bookPanel);

        JPanel authorPanel = new JPanel(new BorderLayout());
        authorModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        JTable authorTable = new JTable(authorModel);
        authorPanel.add(new JScrollPane(authorTable), BorderLayout.CENTER);

        JPanel authorButtons = new JPanel();
        JButton addAuthorBtn = new JButton("Add Author");
        JButton editAuthorBtn = new JButton("Edit Author");
        JButton delAuthorBtn = new JButton("Delete Author");
        JButton refreshAuthorBtn = new JButton("Refresh");
        authorButtons.add(addAuthorBtn);
        authorButtons.add(editAuthorBtn);
        authorButtons.add(delAuthorBtn);
        authorButtons.add(refreshAuthorBtn);
        authorPanel.add(authorButtons, BorderLayout.SOUTH);

        tabs.addTab("Authors", authorPanel);

        JPanel genrePanel = new JPanel(new BorderLayout());
        genreModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        JTable genreTable = new JTable(genreModel);
        genrePanel.add(new JScrollPane(genreTable), BorderLayout.CENTER);

        JPanel genreButtons = new JPanel();
        JButton addGenreBtn = new JButton("Add Genre");
        JButton editGenreBtn = new JButton("Edit Genre");
        JButton delGenreBtn = new JButton("Delete Genre");
        JButton refreshGenreBtn = new JButton("Refresh");
        genreButtons.add(addGenreBtn);
        genreButtons.add(editGenreBtn);
        genreButtons.add(delGenreBtn);
        genreButtons.add(refreshGenreBtn);
        genrePanel.add(genreButtons, BorderLayout.SOUTH);

        tabs.addTab("Genres", genrePanel);

        add(tabs);

        refreshBookBtn.addActionListener(e -> loadBooks());
        addBookBtn.addActionListener(e -> addOrEditBook(null));
        editBookBtn.addActionListener(e -> {
            int sel = bookTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select a book");
                return;
            }
            int id = (int) bookModel.getValueAt(sel, 0);
            addOrEditBook(id);
        });
        delBookBtn.addActionListener(e -> {
            int sel = bookTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select a book");
                return;
            }
            int id = (int) bookModel.getValueAt(sel, 0);
            deleteBook(id);
        });

        refreshAuthorBtn.addActionListener(e -> loadAuthors());
        addAuthorBtn.addActionListener(e -> addOrEditAuthor(null));
        editAuthorBtn.addActionListener(e -> {
            int sel = authorTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select an author");
                return;
            }
            int id = (int) authorModel.getValueAt(sel, 0);
            addOrEditAuthor(id);
        });
        delAuthorBtn.addActionListener(e -> {
            int sel = authorTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select an author");
                return;
            }
            int id = (int) authorModel.getValueAt(sel, 0);
            deleteAuthor(id);
        });

        refreshGenreBtn.addActionListener(e -> loadGenres());
        addGenreBtn.addActionListener(e -> addOrEditGenre(null));
        editGenreBtn.addActionListener(e -> {
            int sel = genreTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select a genre");
                return;
            }
            int id = (int) genreModel.getValueAt(sel, 0);
            addOrEditGenre(id);
        });
        delGenreBtn.addActionListener(e -> {
            int sel = genreTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Select a genre");
                return;
            }
            int id = (int) genreModel.getValueAt(sel, 0);
            deleteGenre(id);
        });

        loadBooks();
        loadAuthors();
        loadGenres();

        setVisible(true);
    }

    private void loadBooks() {
        bookModel.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM books")) {
            while (rs.next()) {
                int bookId = rs.getInt("id");

                String authors = "";
                try (PreparedStatement psA = conn.prepareStatement(
                        "SELECT a.name FROM authors a " +
                                "JOIN book_authors ba ON a.id = ba.author_id " +
                                "WHERE ba.book_id=?")) {
                    psA.setInt(1, bookId);
                    ResultSet rsA = psA.executeQuery();
                    List<String> authorList = new ArrayList<>();
                    while (rsA.next()) authorList.add(rsA.getString("name"));
                    authors = String.join(", ", authorList);
                }

                String genres = "";
                try (PreparedStatement psG = conn.prepareStatement(
                        "SELECT g.name FROM genres g " +
                                "JOIN book_genres bg ON g.id = bg.genre_id " +
                                "WHERE bg.book_id=?")) {
                    psG.setInt(1, bookId);
                    ResultSet rsG = psG.executeQuery();
                    List<String> genreList = new ArrayList<>();
                    while (rsG.next()) genreList.add(rsG.getString("name"));
                    genres = String.join(", ", genreList);
                }

                bookModel.addRow(new Object[]{
                        bookId,
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getInt("quantity"),
                        authors,
                        genres
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrEditBook(Integer bookId) {
        JTextField titleF = new JTextField();
        JTextField yearF = new JTextField();
        JTextField qtyF = new JTextField();

        if (bookId != null) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE id=?")) {
                ps.setInt(1, bookId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    titleF.setText(rs.getString("title"));
                    yearF.setText(String.valueOf(rs.getInt("year")));
                    qtyF.setText(String.valueOf(rs.getInt("quantity")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        List<Integer> authorIds = new ArrayList<>();
        List<String> authorNames = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM authors")) {
            while (rs.next()) {
                authorIds.add(rs.getInt("id"));
                authorNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Integer> genreIds = new ArrayList<>();
        List<String> genreNames = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM genres")) {
            while (rs.next()) {
                genreIds.add(rs.getInt("id"));
                genreNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JList<String> authorList = new JList<>(authorNames.toArray(new String[0]));
        authorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JList<String> genreList = new JList<>(genreNames.toArray(new String[0]));
        genreList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        if (bookId != null) {
            try (PreparedStatement psA = conn.prepareStatement(
                    "SELECT author_id FROM book_authors WHERE book_id=?")) {
                psA.setInt(1, bookId);
                ResultSet rsA = psA.executeQuery();
                List<Integer> sel = new ArrayList<>();
                while (rsA.next()) sel.add(rsA.getInt("author_id"));
                int[] selIdx = sel.stream().mapToInt(a -> authorIds.indexOf(a)).filter(i -> i >= 0).toArray();
                authorList.setSelectedIndices(selIdx);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (PreparedStatement psG = conn.prepareStatement(
                    "SELECT genre_id FROM book_genres WHERE book_id=?")) {
                psG.setInt(1, bookId);
                ResultSet rsG = psG.executeQuery();
                List<Integer> sel = new ArrayList<>();
                while (rsG.next()) sel.add(rsG.getInt("genre_id"));
                int[] selIdx = sel.stream().mapToInt(a -> genreIds.indexOf(a)).filter(i -> i >= 0).toArray();
                genreList.setSelectedIndices(selIdx);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Object[] fields = {
                "Title:", titleF,
                "Year:", yearF,
                "Quantity:", qtyF,
                "Authors:", new JScrollPane(authorList),
                "Genres:", new JScrollPane(genreList)
        };

        int option = JOptionPane.showConfirmDialog(this, fields,
                bookId == null ? "Add Book" : "Edit Book", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                conn.setAutoCommit(false);

                if (bookId == null) {
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO books(title,year,quantity) VALUES(?,?,?)",
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, titleF.getText());
                    ps.setInt(2, Integer.parseInt(yearF.getText()));
                    ps.setInt(3, Integer.parseInt(qtyF.getText()));
                    ps.executeUpdate();
                    ResultSet keys = ps.getGeneratedKeys();
                    keys.next();
                    bookId = keys.getInt(1);

                    for (int idx : authorList.getSelectedIndices()) {
                        int authorId = authorIds.get(idx);
                        PreparedStatement psA = conn.prepareStatement(
                                "INSERT INTO book_authors(book_id, author_id) VALUES(?,?)");
                        psA.setInt(1, bookId);
                        psA.setInt(2, authorId);
                        psA.executeUpdate();
                    }

                    for (int idx : genreList.getSelectedIndices()) {
                        int genreId = genreIds.get(idx);
                        PreparedStatement psG = conn.prepareStatement(
                                "INSERT INTO book_genres(book_id, genre_id) VALUES(?,?)");
                        psG.setInt(1, bookId);
                        psG.setInt(2, genreId);
                        psG.executeUpdate();
                    }

                } else {
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE books SET title=?, year=?, quantity=? WHERE id=?");
                    ps.setString(1, titleF.getText());
                    ps.setInt(2, Integer.parseInt(yearF.getText()));
                    ps.setInt(3, Integer.parseInt(qtyF.getText()));
                    ps.setInt(4, bookId);
                    ps.executeUpdate();

                    for (int idx : authorList.getSelectedIndices()) {
                        int authorId = authorIds.get(idx);
                        PreparedStatement check = conn.prepareStatement(
                                "SELECT COUNT(*) FROM book_authors WHERE book_id=? AND author_id=?");
                        check.setInt(1, bookId);
                        check.setInt(2, authorId);
                        ResultSet rsC = check.executeQuery();
                        rsC.next();
                        if (rsC.getInt(1) == 0) {
                            PreparedStatement psA = conn.prepareStatement(
                                    "INSERT INTO book_authors(book_id, author_id) VALUES(?,?)");
                            psA.setInt(1, bookId);
                            psA.setInt(2, authorId);
                            psA.executeUpdate();
                        }
                    }

                    for (int idx : genreList.getSelectedIndices()) {
                        int genreId = genreIds.get(idx);
                        PreparedStatement check = conn.prepareStatement(
                                "SELECT COUNT(*) FROM book_genres WHERE book_id=? AND genre_id=?");
                        check.setInt(1, bookId);
                        check.setInt(2, genreId);
                        ResultSet rsC = check.executeQuery();
                        rsC.next();
                        if (rsC.getInt(1) == 0) {
                            PreparedStatement psG = conn.prepareStatement(
                                    "INSERT INTO book_genres(book_id, genre_id) VALUES(?,?)");
                            psG.setInt(1, bookId);
                            psG.setInt(2, genreId);
                            psG.executeUpdate();
                        }
                    }
                }

                conn.commit();
                conn.setAutoCommit(true);
                loadBooks();

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    private void deleteBook(int bookId) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAuthors() {
        authorModel.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM authors")) {
            while (rs.next()) {
                authorModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrEditAuthor(Integer authorId) {
        JTextField nameF = new JTextField();
        if (authorId != null) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM authors WHERE id=?")) {
                ps.setInt(1, authorId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) nameF.setText(rs.getString("name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Object[] fields = {"Name:", nameF};
        int option = JOptionPane.showConfirmDialog(this, fields,
                authorId == null ? "Add Author" : "Edit Author", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (authorId == null) {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO authors(name) VALUES(?)");
                    ps.setString(1, nameF.getText());
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement("UPDATE authors SET name=? WHERE id=?");
                    ps.setString(1, nameF.getText());
                    ps.setInt(2, authorId);
                    ps.executeUpdate();
                }
                loadAuthors();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteAuthor(int authorId) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM authors WHERE id=?")) {
            ps.setInt(1, authorId);
            ps.executeUpdate();
            loadAuthors();
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGenres() {
        genreModel.setRowCount(0);
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM genres")) {
            while (rs.next()) {
                genreModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addOrEditGenre(Integer genreId) {
        JTextField nameF = new JTextField();
        if (genreId != null) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM genres WHERE id=?")) {
                ps.setInt(1, genreId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) nameF.setText(rs.getString("name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Object[] fields = {"Name:", nameF};
        int option = JOptionPane.showConfirmDialog(this, fields,
                genreId == null ? "Add Genre" : "Edit Genre", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                if (genreId == null) {
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO genres(name) VALUES(?)");
                    ps.setString(1, nameF.getText());
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement("UPDATE genres SET name=? WHERE id=?");
                    ps.setString(1, nameF.getText());
                    ps.setInt(2, genreId);
                    ps.executeUpdate();
                }
                loadGenres();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteGenre(int genreId) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM genres WHERE id=?")) {
            ps.setInt(1, genreId);
            ps.executeUpdate();
            loadGenres();
            loadBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryAppFull::new);
    }
}
