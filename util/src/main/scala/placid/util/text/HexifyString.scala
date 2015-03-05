package placid
package util
package text

import java.nio.charset.Charset

/**
 *
 * Convert from and to a hexified version of the string's content.
 *
 */
class HexifyString(s: String) {

  /**
   * Converts string to a hex string, the hex characters are all uppercase; it is case sensitive.
   */
  final def hexify(charset: Charset): String = {
    val bytes = s.getBytes(charset)
    val buf = new StringBuilder(2 * bytes.length)
    var i = 0; while (i < bytes.length) { buf.append(hexarray(0xff & bytes(i))); i += 1 }
    buf.toString
  }

  /**
   * Converts to hexified assuming an UTF-8 encoded string.
   */
  final def hexify: String = hexify(`UTF-8`)

  /**
   * Converts from a hexified to a string encoded with @charset; it is case insensitive (hex can be upper- or lowercase); hex length must be even.
   */
  final def unhexify(charset: Charset): String = {
    val len = s.length / 2
    val buf = new Array[Byte](len)
    var i = 0
    while (i < len) {
      val j = 2 * i
      buf(i) = ((Character.digit(s.charAt(j), 16) << 4) + Character.digit(s.charAt(j + 1), 16)).toByte
      i += 1
    }
    new String(buf, charset)
  }

  final def unhexify: String = unhexify(`UTF-8`)

  /**
   *
   */
  private[this] final val hexarray = Array("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF")

  //  private[this] final val hexcryptarray = Array(
  //    "7D", "C8", "EB", "1E", "96", "77", "ED", "62", "44", "80", "92", "59", "FC", "B7", "14", "15", "9A", "6E", "A7", "D3", "C7", "79", "D0", "36", "D2", "F6", "52", "42", "DB", "A6", "90", "48", "70", "A0", "2B", "00", "21", "3C", "61", "F5", "B9", "A3", "BE", "AD", "50", "AF", "91", "0A", "3E", "45", "08", "16", "28", "01", "57", "23", "99", "EE", "AE", "DF", "8E", "D7", "B5", "5B", "64", "8B", "CA", "0D", "4F", "32", "A5", "DE", "93", "BA", "F4", "30", "D1", "78", "18", "75", "C3", "E5", "6D", "09", "2D", "63", "34", "49", "D8", "8C", "2C", "40", "E8", "A8", "A2", "D6", "DC", "12", "95", "68", "87", "06", "72", "53", "5E", "8D", "CE", "6A", "6B", "8A", "11", "E2", "9B", "55", "1B", "6C", "31", "C5", "76", "E9", "66", "CD", "39", "EA", "EC", "24", "1C", "5D", "47", "5C", "60", "84", "0B", "BC", "2A", "FB", "C0", "DA", "73", "98", "0C", "4B", "25", "E3", "CF", "E1", "D9", "69", "B8", "43", "F9", "FD", "D4", "3B", "4A", "41", "F7", "26", "6F", "83", "05", "10", "A1", "58", "38", "BB", "3D", "C4", "3A", "C9", "89", "5F", "C6", "51", "1A", "88", "B6", "7A", "0E", "81", "AB", "5A", "BF", "DD", "B0", "04", "94", "07", "B4", "8F", "CB", "AC", "56", "19", "E4", "7B", "37", "27", "F0", "A9", "B3", "4E", "97", "1D", "BD", "86", "4C", "0F", "D5", "35", "13", "02", "F3", "29", "B1", "AA", "A4", "67", "B2", "9F", "F1", "17", "9D", "4D", "85", "65", "03", "54", "FE", "74", "C1", "E0", "7F", "7C", "2E", "C2", "FF", "20", "82", "CC", "71", "7E", "F2", "F8", "2F", "9E", "E6", "9C", "33", "3F", "22", "1F", "E7", "FA", "46", "EF",
  //    "CC", "00", "78", "0E", "76", "B6", "EF", "AA", "CB", "D5", "9F", "91", "E4", "EA", "3F", "81", "30", "F2", "70", "8C", "6A", "5D", "33", "4D", "05", "BB", "08", "F1", "44", "90", "B4", "96", "61", "E3", "B2", "E9", "26", "10", "35", "56", "A6", "77", "6C", "C8", "1E", "0D", "BD", "AC", "31", "12", "99", "C9", "27", "F0", "4F", "7D", "C4", "D0", "43", "3B", "D2", "0B", "36", "E1", "03", "8B", "DB", "14", "4B", "AF", "E5", "11", "EE", "F8", "8A", "48", "FB", "40", "62", "CA", "F6", "A3", "58", "A0", "6E", "F9", "C6", "38", "20", "64", "69", "5A", "7F", "86", "45", "5B", "E7", "AB", "FA", "72", "4C", "B8", "92", "13", "01", "15", "ED", "46", "83", "1B", "04", "57", "C3", "E2", "24", "55", "D8", "6F", "E8", "84", "09", "66", "79", "C0", "CD", "CE", "65", "F3", "16", "DE", "06", "D9", "63", "B0", "67", "F4", "52", "9D", "95", "EC", "3D", "B7", "5F", "BF", "87", "53", "C1", "F5", "1C", "74", "28", "6D", "1A", "47", "F7", "4A", "3A", "A4", "02", "23", "89", "7B", "2D", "2E", "25", "73", "C7", "07", "17", "B1", "51", "E6", "FE", "BE", "A2", "29", "EB", "37", "71", "5C", "93", "DA", "8E", "97", "88", "2F", "0C", "3E", "FC", "60", "68", "D4", "FD", "D1", "49", "9C", "AD", "8D", "B9", "B5", "6B", "54", "2C", "3C", "42", "4E", "98", "82", "DD", "22", "2A", "50", "A8", "A1", "80", "D3", "7A", "18", "59", "CF", "E0", "7C", "7E", "9B", "DF", "21", "5E", "D6", "0F", "0A", "8F", "41", "DC", "C2", "A9", "39", "1D", "BC", "9E", "1F", "19", "BA", "94", "9A", "34", "B3", "32", "A5", "A7", "2B", "AE", "75", "D7", "C5", "85", "FF",
  //    "2F", "9E", "F1", "92", "74", "ED", "13", "E1", "7E", "8D", "E7", "A2", "50", "FC", "33", "0B", "61", "E9", "73", "19", "A5", "B8", "56", "CC", "27", "9A", "64", "97", "3A", "04", "BD", "07", "BA", "7A", "8E", "81", "C7", "63", "AA", "C9", "3D", "BB", "CB", "01", "52", "C5", "9B", "38", "4E", "A8", "FE", "BF", "4D", "8A", "39", "21", "6B", "C4", "E3", "89", "77", "3E", "B3", "1B", "54", "C8", "AB", "D6", "18", "9C", "4A", "91", "B5", "C2", "AD", "00", "29", "F8", "8C", "32", "98", "65", "5A", "B0", "DA", "84", "14", "43", "51", "49", "67", "C0", "2B", "B2", "DF", "1C", "D5", "08", "83", "3F", "68", "1F", "25", "EF", "40", "1A", "C1", "B4", "5F", "58", "A1", "F0", "FA", "E2", "05", "D9", "E4", "0E", "6A", "7C", "F5", "87", "C3", "2C", "6C", "EE", "A4", "B7", "30", "15", "28", "06", "F9", "02", "0C", "59", "69", "A6", "72", "4C", "FD", "46", "3B", "A3", "F4", "78", "99", "1E", "24", "A0", "0D", "AE", "E5", "90", "8F", "09", "7B", "DD", "12", "22", "36", "1D", "DE", "94", "66", "C6", "E0", "CD", "B1", "88", "71", "0F", "70", "7D", "2A", "DB", "82", "20", "EA", "CF", "EC", "11", "F2", "6F", "31", "F6", "EB", "95", "D4", "4B", "D3", "5B", "80", "85", "16", "BC", "96", "3C", "CE", "D0", "26", "62", "F7", "5C", "5E", "B6", "A9", "93", "35", "42", "D2", "AC", "41", "57", "F3", "FB", "9F", "60", "8B", "10", "44", "7F", "D1", "53", "B9", "CA", "6D", "86", "DC", "23", "55", "37", "D8", "34", "47", "17", "D7", "FF", "45", "E8", "5D", "2D", "6E", "AF", "E6", "03", "0A", "79", "A7", "48", "9D", "BE", "2E", "4F", "76", "75",
  //    "85", "81", "03", "2B", "30", "AA", "C6", "61", "49", "C4", "B5", "0C", "94", "0D", "84", "24", "F2", "B2", "8D", "29", "E8", "66", "6F", "AB", "D4", "FB", "1B", "F4", "40", "C5", "77", "5B", "62", "08", "F9", "F1", "04", "D9", "50", "BC", "98", "D3", "F0", "34", "21", "0F", "5E", "D6", "6A", "16", "5C", "A6", "47", "DC", "87", "AC", "E5", "CF", "C1", "A8", "6E", "F5", "20", "E9", "B3", "67", "42", "9E", "52", "A2", "4B", "EA", "8C", "1E", "09", "8B", "ED", "76", "57", "99", "74", "E0", "E6", "3D", "EF", "CA", "4A", "3B", "F8", "92", "B6", "D0", "2E", "46", "1D", "70", "8F", "D5", "37", "7A", "28", "79", "F7", "A3", "73", "33", "B0", "45", "9F", "3C", "65", "83", "3F", "6D", "C8", "9C", "A0", "2C", "1A", "A1", "91", "FD", "9D", "90", "BD", "0A", "51", "CD", "E3", "36", "05", "4F", "C9", "CB", "14", "DE", "63", "23", "13", "93", "4D", "64", "F3", "35", "10", "7D", "0B", "7E", "D2", "26", "B4", "B1", "22", "EB", "E2", "CE", "58", "68", "DF", "2D", "86", "6B", "00", "5D", "69", "D7", "C2", "C0", "F6", "95", "2F", "78", "FF", "9B", "AF", "39", "1C", "3A", "72", "DA", "A7", "4E", "BB", "8A", "9A", "02", "15", "BA", "8E", "C7", "01", "80", "41", "06", "3E", "55", "75", "D1", "88", "7F", "25", "EC", "89", "43", "60", "6C", "A5", "EE", "FC", "B9", "2A", "53", "E7", "AE", "7C", "C3", "FE", "96", "BE", "A4", "AD", "54", "E1", "CC", "56", "18", "82", "17", "59", "BF", "FA", "19", "1F", "12", "11", "97", "A9", "DB", "07", "E4", "4C", "44", "38", "5A", "B8", "71", "5F", "31", "D8", "DD", "32", "27", "B7", "0E", "7B", "48",
  //    "14", "63", "3A", "06", "B7", "83", "5C", "D7", "E7", "0A", "D2", "04", "3D", "62", "05", "DE", "61", "54", "A4", "58", "C1", "8E", "3E", "4A", "38", "EB", "0F", "23", "D0", "49", "79", "EF", "EC", "FB", "27", "08", "4C", "F6", "ED", "BE", "EA", "F3", "B2", "DB", "CA", "98", "6B", "A3", "BF", "13", "A1", "55", "75", "1D", "A9", "89", "5A", "17", "92", "52", "50", "BA", "B0", "2E", "9C", "F2", "D9", "21", "F7", "16", "C5", "B6", "0B", "01", "26", "A2", "8C", "B4", "5E", "95", "28", "1A", "64", "22", "B3", "CF", "AA", "39", "FC", "D4", "7B", "A6", "C2", "5B", "09", "6F", "E8", "AC", "0D", "E3", "D5", "8A", "2F", "45", "9B", "AE", "E9", "07", "F9", "2C", "9F", "B1", "24", "BB", "C0", "DC", "37", "53", "48", "7C", "70", "B5", "E6", "18", "BC", "F1", "12", "20", "A0", "1B", "D6", "9A", "CE", "11", "80", "2D", "E5", "1C", "AB", "AF", "7F", "59", "19", "88", "91", "E0", "A7", "85", "C7", "03", "00", "67", "7E", "FF", "47", "97", "32", "46", "6C", "F8", "8D", "9E", "FE", "30", "0C", "73", "31", "86", "96", "6D", "74", "BD", "68", "90", "8B", "A8", "EE", "6E", "44", "2B", "C9", "6A", "7D", "72", "3F", "35", "D1", "94", "C8", "4E", "2A", "84", "5D", "29", "1E", "CC", "9D", "D3", "77", "99", "4D", "65", "76", "81", "F5", "71", "FD", "87", "DA", "DD", "33", "4F", "15", "10", "D8", "56", "51", "CD", "8F", "57", "69", "60", "AD", "F4", "43", "66", "42", "3B", "7A", "40", "82", "A5", "1F", "34", "C4", "3C", "0E", "B8", "4B", "B9", "93", "5F", "E4", "CB", "E1", "C6", "C3", "DF", "78", "41", "36", "25", "E2", "F0", "FA", "02",
  //    "37", "10", "71", "F1", "91", "42", "BF", "2F", "30", "40", "38", "50", "4B", "7A", "8F", "36", "C7", "70", "2B", "28", "D5", "3D", "06", "3F", "E0", "C3", "05", "0B", "E9", "AC", "66", "8D", "31", "23", "65", "93", "ED", "98", "EC", "1B", "B9", "7C", "5F", "F7", "2D", "EE", "75", "AE", "22", "1C", "44", "DC", "FE", "EB", "6F", "48", "64", "FB", "C6", "61", "D6", "C9", "67", "C4", "B5", "78", "9F", "DE", "26", "00", "BD", "1A", "3E", "84", "62", "35", "39", "21", "0E", "1E", "4A", "E1", "56", "54", "33", "87", "A5", "FD", "CB", "24", "2C", "CD", "18", "F4", "14", "E7", "04", "9E", "CF", "A0", "D8", "68", "3C", "27", "EF", "D9", "94", "E5", "5D", "F5", "57", "59", "9D", "81", "0A", "C1", "B8", "AD", "F8", "A8", "5C", "32", "F2", "E2", "DD", "D0", "C0", "25", "29", "F9", "79", "A7", "2A", "8C", "72", "80", "4E", "8B", "F0", "49", "E4", "4D", "47", "0F", "6E", "AB", "E8", "69", "1D", "73", "17", "15", "B0", "16", "7D", "BC", "76", "12", "55", "8E", "08", "CA", "6B", "CC", "13", "B3", "9B", "4C", "63", "5B", "34", "A3", "A1", "DF", "43", "60", "41", "2E", "BB", "52", "4F", "86", "FF", "F3", "E6", "CE", "7E", "6C", "6A", "3A", "D4", "58", "B1", "74", "DB", "89", "A9", "BA", "9C", "9A", "F6", "BE", "A4", "11", "D2", "A6", "A2", "53", "88", "E3", "7B", "19", "C2", "96", "FA", "D3", "7F", "92", "B2", "01", "D1", "09", "02", "B6", "D7", "B4", "95", "03", "C5", "FC", "5E", "90", "82", "AA", "83", "AF", "EA", "3B", "51", "20", "99", "85", "1F", "5A", "6D", "07", "C8", "77", "0D", "8A", "0C", "B7", "45", "DA", "97", "46",
  //    "79", "E1", "43", "15", "31", "1B", "BE", "6B", "37", "B9", "7B", "E7", "9D", "8A", "07", "4E", "DB", "90", "E6", "54", "2D", "52", "6C", "12", "F6", "34", "3C", "A0", "14", "DC", "E4", "01", "7E", "62", "A2", "29", "C0", "4F", "7F", "25", "2F", "6F", "0A", "56", "97", "FB", "D4", "28", "68", "D3", "BA", "F3", "53", "CD", "18", "CC", "1E", "51", "AB", "08", "A3", "83", "4A", "6A", "D7", "B1", "87", "3D", "EF", "16", "65", "DE", "E0", "7D", "0C", "DF", "71", "03", "06", "04", "7C", "C4", "60", "E9", "3A", "20", "22", "A8", "BB", "33", "24", "E2", "C6", "B2", "2C", "F7", "2B", "72", "0F", "FF", "A9", "92", "5C", "A5", "99", "23", "5B", "A6", "F2", "02", "58", "CE", "D5", "9B", "9A", "DA", "FC", "C2", "F9", "E3", "ED", "BF", "80", "D0", "FD", "47", "AC", "67", "9E", "C7", "AF", "C9", "91", "35", "D2", "A1", "73", "2A", "3F", "A7", "32", "75", "38", "1D", "11", "39", "F4", "05", "36", "85", "8F", "6D", "94", "1A", "AD", "64", "55", "A4", "76", "6E", "5A", "9C", "77", "CF", "5D", "F1", "88", "57", "3B", "EA", "4B", "61", "E5", "E8", "49", "86", "CB", "D9", "09", "8C", "F0", "69", "44", "0B", "63", "42", "F5", "7A", "C3", "1F", "BC", "78", "70", "9F", "EC", "00", "D8", "B0", "74", "17", "10", "40", "BD", "F8", "EB", "5E", "B6", "FE", "19", "AE", "4C", "96", "84", "21", "D6", "3E", "CA", "98", "41", "FA", "4D", "46", "0E", "1C", "81", "30", "59", "27", "EE", "50", "B4", "C8", "2E", "82", "93", "C1", "B5", "C5", "B7", "95", "DD", "48", "89", "13", "B8", "45", "66", "8E", "5F", "26", "D1", "8B", "8D", "B3", "AA", "0D",
  //    "23", "2C", "18", "75", "44", "B8", "D6", "D1", "30", "D4", "C0", "BA", "95", "25", "D9", "A0", "A9", "32", "60", "67", "57", "1E", "42", "8A", "4F", "8F", "1C", "2A", "FF", "76", "0C", "BE", "00", "56", "28", "74", "BD", "97", "7B", "40", "F1", "CD", "3F", "0A", "4C", "33", "6F", "47", "4E", "D0", "0D", "9F", "2B", "83", "C3", "AE", "A5", "62", "E8", "26", "C4", "DA", "31", "2E", "BB", "CA", "91", "99", "CB", "41", "B3", "E5", "CE", "45", "E2", "1F", "9B", "13", "CC", "ED", "DF", "17", "21", "5A", "16", "F3", "8C", "EB", "4D", "09", "08", "43", "69", "24", "EF", "07", "64", "8E", "1D", "C1", "8D", "02", "AA", "61", "DD", "3D", "11", "D7", "FE", "7F", "E4", "48", "DB", "54", "27", "37", "03", "6E", "F4", "6C", "A7", "6A", "38", "B0", "A4", "65", "72", "C9", "34", "BC", "B2", "AC", "68", "FB", "8B", "52", "90", "E3", "F2", "9D", "C7", "1B", "FA", "D2", "EC", "DC", "BF", "6B", "59", "C6", "77", "9E", "5C", "06", "87", "A3", "80", "29", "96", "88", "93", "0F", "82", "10", "50", "A6", "22", "C2", "73", "14", "5F", "12", "15", "70", "EA", "C5", "EE", "C8", "81", "E1", "0B", "E7", "53", "B5", "B7", "D8", "6D", "7D", "78", "01", "20", "19", "CF", "E0", "5E", "05", "2F", "D3", "63", "F9", "A1", "D5", "98", "71", "55", "04", "94", "7E", "F5", "0E", "1A", "5B", "49", "86", "4B", "B6", "79", "FC", "E9", "AD", "3A", "84", "7C", "B1", "46", "58", "FD", "F0", "F8", "9C", "A8", "51", "35", "3E", "B9", "AB", "39", "4A", "85", "F6", "3B", "9A", "92", "5D", "A2", "7A", "AF", "2D", "DE", "3C", "E6", "B4", "36", "66", "89", "F7",
  //    "4E", "18", "E9", "85", "51", "C1", "94", "8E", "2E", "48", "7D", "A1", "4D", "78", "3E", "DA", "A2", "A8", "EF", "A7", "AD", "36", "4F", "F4", "69", "31", "B1", "F2", "E4", "9E", "37", "9A", "6F", "B9", "ED", "96", "1A", "9B", "6A", "1B", "DF", "DB", "C3", "00", "15", "9F", "FE", "86", "3D", "88", "75", "5F", "C4", "46", "68", "2C", "95", "49", "7E", "39", "06", "62", "A5", "FD", "02", "CA", "27", "34", "EB", "AB", "E8", "D0", "5E", "F9", "13", "6D", "AF", "7C", "16", "E2", "C2", "5C", "B7", "8A", "76", "2D", "25", "23", "C8", "64", "BB", "1E", "22", "29", "73", "DD", "3A", "B2", "50", "A3", "14", "10", "2F", "1F", "BE", "CF", "65", "1C", "D3", "F7", "53", "05", "F1", "21", "0D", "6C", "20", "6E", "54", "1D", "42", "81", "BC", "74", "BA", "59", "9D", "5A", "A6", "72", "41", "FB", "FF", "A4", "30", "0E", "35", "89", "BF", "B4", "C6", "60", "4C", "77", "44", "EC", "92", "F5", "57", "66", "D7", "D6", "28", "F0", "D1", "56", "9C", "4A", "63", "D2", "82", "40", "45", "26", "D4", "E3", "E7", "CC", "0C", "DE", "EE", "B0", "8F", "E6", "55", "B8", "3B", "B3", "FA", "C7", "11", "80", "8D", "6B", "52", "8C", "70", "2B", "A9", "71", "F6", "0A", "AA", "5D", "43", "A0", "CD", "07", "B5", "38", "EA", "67", "90", "E5", "03", "2A", "C9", "F8", "24", "47", "C5", "B6", "BD", "12", "3C", "09", "93", "8B", "4B", "0F", "79", "58", "E0", "E1", "99", "91", "7B", "17", "32", "84", "D9", "83", "97", "0B", "F3", "D5", "DC", "3F", "CB", "7A", "AC", "5B", "AE", "19", "7F", "CE", "08", "33", "D8", "C0", "98", "04", "FC", "01", "87", "61",
  //    "58", "D7", "A5", "5E", "0E", "DB", "53", "F6", "DF", "79", "D8", "92", "C9", "A0", "54", "90", "A2", "B2", "FB", "FF", "47", "52", "91", "BC", "16", "7B", "AA", "96", "DE", "9B", "94", "97", "B5", "59", "32", "04", "21", "55", "D4", "B6", "77", "99", "6E", "E4", "A1", "1E", "F2", "D0", "85", "61", "51", "65", "39", "6C", "1D", "8E", "33", "E9", "13", "E5", "A9", "F0", "66", "3D", "95", "20", "9A", "71", "4A", "0B", "88", "60", "8F", "8D", "49", "4F", "11", "F1", "45", "F4", "C7", "EF", "D3", "CB", "48", "B1", "93", "2E", "E6", "38", "EC", "08", "01", "B7", "26", "ED", "2F", "F5", "14", "CE", "46", "B8", "F7", "00", "29", "15", "A7", "73", "9C", "42", "FA", "FC", "C2", "69", "72", "3E", "F9", "9E", "A4", "6B", "4D", "83", "27", "07", "DD", "E2", "C5", "D2", "BB", "64", "BF", "30", "2A", "DA", "2B", "BD", "C1", "68", "CF", "CD", "37", "7A", "AD", "D6", "1F", "B0", "67", "B3", "40", "0A", "22", "B9", "81", "76", "84", "82", "70", "FE", "89", "D5", "86", "1A", "C6", "06", "A8", "02", "4E", "CA", "E3", "A3", "0D", "4B", "80", "5A", "19", "2C", "E7", "F8", "23", "87", "31", "7E", "7C", "44", "2D", "AB", "DC", "5C", "F3", "35", "43", "78", "D9", "5B", "10", "9F", "12", "3B", "C0", "E1", "63", "05", "41", "24", "D1", "36", "18", "EE", "25", "28", "03", "8A", "8B", "C3", "AC", "AF", "9D", "7D", "6F", "6A", "5F", "A6", "17", "4C", "62", "3A", "C4", "C8", "AE", "3C", "EA", "5D", "1B", "56", "50", "EB", "6D", "CC", "75", "09", "7F", "E8", "0F", "0C", "B4", "57", "98", "E0", "34", "3F", "8C", "1C", "BA", "FD", "BE", "74",
  //    "61", "85", "A8", "AB", "9B", "55", "23", "D8", "AF", "81", "12", "80", "10", "B5", "FD", "A4", "F9", "00", "FE", "14", "DE", "D2", "0C", "4F", "96", "86", "AD", "B7", "A2", "46", "0D", "54", "9D", "02", "5B", "4B", "48", "FB", "73", "32", "CA", "0E", "2E", "9A", "EC", "3C", "1E", "5A", "06", "19", "4D", "37", "74", "B8", "3A", "7C", "56", "34", "84", "E0", "DF", "F4", "A9", "EB", "42", "6A", "CF", "DD", "D0", "F1", "15", "57", "E6", "5C", "8B", "07", "94", "7B", "43", "EA", "52", "83", "6E", "1A", "45", "98", "C1", "A3", "0A", "FC", "5F", "88", "97", "36", "82", "D3", "17", "BB", "58", "29", "DC", "18", "30", "B3", "9F", "3E", "9C", "33", "2D", "D4", "2B", "BE", "F0", "D7", "BD", "ED", "05", "03", "68", "7F", "EF", "CD", "CB", "C7", "63", "B0", "6D", "F5", "B4", "BF", "C6", "8D", "6F", "B2", "08", "72", "BC", "4E", "9E", "27", "A1", "D5", "20", "79", "D9", "75", "93", "A5", "90", "1D", "CC", "51", "C4", "7D", "2A", "F6", "C2", "69", "EE", "66", "2F", "53", "8C", "D1", "0F", "25", "67", "AC", "C5", "F2", "6B", "70", "2C", "92", "26", "65", "A7", "E9", "AA", "E5", "40", "01", "04", "C9", "B6", "3B", "B1", "38", "76", "09", "5D", "7A", "91", "E7", "3D", "C0", "FF", "BA", "60", "59", "11", "A0", "4A", "1B", "41", "22", "E4", "C3", "D6", "3F", "0B", "8E", "E2", "89", "F8", "50", "31", "21", "1F", "78", "CE", "6C", "24", "AE", "DB", "E1", "35", "B9", "87", "47", "7E", "8A", "95", "16", "F3", "44", "71", "8F", "62", "4C", "E8", "DA", "64", "A6", "49", "E3", "1C", "77", "99", "5E", "28", "13", "F7", "39", "C8", "FA",
  //    "68", "E4", "BD", "AA", "4F", "11", "C5", "7E", "CA", "51", "D1", "47", "14", "0E", "58", "6B", "22", "86", "56", "2B", "34", "90", "EE", "80", "E0", "CE", "A5", "2E", "6C", "B0", "41", "76", "92", "BF", "81", "24", "DC", "4A", "78", "23", "5E", "84", "8A", "27", "08", "16", "9A", "28", "9C", "1D", "C7", "AF", "7B", "FC", "09", "9E", "5B", "67", "3D", "7C", "EB", "17", "13", "39", "A2", "62", "DE", "E1", "DF", "98", "DD", "33", "12", "42", "00", "37", "E8", "01", "77", "95", "57", "88", "B3", "38", "45", "63", "F6", "D0", "59", "5C", "FF", "4B", "8E", "F1", "C0", "AC", "74", "D6", "99", "06", "5A", "C3", "A1", "4C", "E5", "0B", "53", "CF", "3B", "E2", "F9", "5D", "2D", "E3", "F4", "3E", "6E", "21", "9F", "0F", "F5", "91", "46", "BB", "8D", "C9", "B2", "A4", "D9", "F3", "B7", "36", "C1", "C2", "6A", "B9", "1F", "66", "49", "7D", "E9", "03", "B8", "1E", "54", "A3", "19", "3F", "97", "BA", "02", "EA", "AE", "94", "D5", "52", "87", "89", "AB", "48", "2F", "7F", "6D", "A7", "64", "CC", "6F", "10", "A0", "BE", "ED", "5F", "30", "9D", "35", "26", "9B", "2A", "85", "1B", "43", "B1", "F2", "72", "C6", "0D", "FD", "07", "8B", "20", "C4", "73", "FB", "FE", "93", "F0", "96", "EC", "D3", "61", "D7", "05", "D8", "AD", "4D", "32", "71", "7A", "A6", "40", "3C", "F7", "82", "4E", "44", "BC", "25", "DB", "8C", "0A", "83", "15", "8F", "FA", "D2", "3A", "79", "31", "69", "EF", "1C", "F8", "75", "2C", "65", "60", "0C", "50", "55", "18", "A8", "DA", "CB", "D4", "A9", "B6", "1A", "B5", "29", "C8", "04", "B4", "E7", "CD", "70", "E6",
  //    "43", "01", "FB", "76", "1A", "0E", "64", "C1", "00", "95", "07", "FF", "D7", "08", "BC", "A8", "0C", "A3", "C3", "4E", "96", "10", "3D", "24", "F1", "BE", "69", "B6", "FE", "F8", "57", "E1", "31", "0D", "DD", "A0", "CD", "2F", "AC", "B9", "E4", "EE", "12", "5E", "D1", "65", "C6", "CE", "EF", "9D", "CC", "AB", "6E", "22", "86", "9A", "F6", "D5", "71", "9F", "CA", "7A", "66", "0A", "0B", "11", "7F", "F5", "63", "EA", "9E", "4F", "4D", "53", "AD", "E9", "74", "55", "8D", "A4", "F4", "34", "DA", "C9", "97", "6D", "C5", "18", "8B", "B2", "36", "39", "6F", "A9", "9C", "90", "52", "B8", "80", "D3", "2A", "B4", "A2", "A7", "F0", "1F", "28", "5B", "41", "DF", "3F", "62", "77", "03", "D8", "E7", "87", "E5", "1C", "49", "67", "92", "F3", "23", "BD", "B1", "D6", "85", "72", "D9", "84", "68", "61", "C4", "13", "1B", "7E", "06", "19", "1E", "FA", "AE", "04", "E6", "B3", "A6", "5A", "ED", "37", "15", "14", "89", "98", "27", "60", "C0", "70", "82", "8E", "E2", "DB", "45", "C8", "56", "44", "EC", "4A", "C7", "20", "4C", "33", "EB", "46", "6A", "BF", "88", "E0", "FD", "2B", "79", "54", "CF", "3C", "1D", "93", "5C", "3E", "A1", "7C", "78", "91", "83", "FC", "09", "42", "4B", "94", "6B", "3A", "38", "8C", "58", "59", "2C", "2E", "2D", "F2", "21", "50", "9B", "BB", "5D", "B7", "A5", "30", "D4", "DE", "E3", "32", "C2", "6C", "7B", "75", "3B", "47", "D2", "02", "29", "48", "35", "F9", "B5", "AA", "DC", "81", "8F", "BA", "99", "16", "73", "5F", "F7", "0F", "05", "8A", "CB", "51", "B0", "26", "AF", "D0", "40", "25", "7D", "17", "E8",
  //    "80", "E6", "65", "D6", "43", "27", "50", "45", "4B", "8C", "22", "67", "3D", "3F", "16", "5D", "C4", "FF", "F1", "BC", "3C", "62", "28", "7B", "D8", "09", "1B", "94", "0D", "15", "24", "2E", "13", "10", "DE", "49", "EA", "38", "05", "4E", "AE", "97", "63", "2D", "0F", "F2", "47", "11", "A8", "FE", "C8", "D9", "86", "AD", "71", "E4", "E7", "68", "46", "C7", "21", "74", "37", "33", "F6", "60", "8B", "BB", "8E", "B3", "4A", "40", "B0", "78", "56", "AB", "69", "9D", "5A", "D1", "34", "93", "F3", "36", "E0", "A3", "1F", "BA", "77", "ED", "B7", "14", "48", "D3", "90", "44", "91", "84", "CB", "2B", "99", "0B", "0A", "9A", "1E", "76", "AC", "CF", "A2", "0C", "C3", "DC", "FA", "18", "96", "29", "A5", "79", "72", "A7", "FC", "AF", "BD", "D7", "C0", "B6", "DA", "A6", "6C", "0E", "D0", "B5", "26", "5F", "A9", "53", "EE", "4F", "D4", "B9", "1D", "A4", "F7", "19", "6B", "55", "51", "F0", "DD", "F4", "17", "3A", "BF", "EB", "2F", "CE", "9E", "42", "01", "9B", "59", "D5", "F8", "E9", "7E", "C9", "70", "73", "32", "06", "82", "12", "35", "EC", "AA", "6F", "07", "88", "6D", "31", "95", "00", "3E", "B1", "E5", "1A", "5E", "8A", "FB", "58", "6E", "C1", "6A", "9F", "64", "04", "87", "02", "F9", "7C", "B2", "7D", "85", "E8", "9C", "CC", "54", "E2", "1C", "25", "66", "08", "5C", "C6", "CA", "7A", "81", "E1", "2A", "83", "4C", "4D", "23", "41", "FD", "8F", "39", "C5", "57", "E3", "DF", "20", "75", "EF", "98", "89", "BE", "2C", "DB", "92", "7F", "52", "3B", "B8", "CD", "A0", "03", "B4", "F5", "30", "5B", "8D", "C2", "61", "D2", "A1",
  //    "C2", "6F", "79", "84", "82", "CF", "1D", "5B", "CD", "E4", "61", "02", "16", "AD", "45", "1E", "F9", "42", "2D", "07", "5E", "A1", "48", "F6", "A7", "9C", "20", "50", "B3", "7E", "01", "54", "66", "8B", "98", "AA", "5D", "31", "52", "3B", "33", "39", "4F", "3F", "94", "8F", "BA", "F7", "CC", "86", "9D", "26", "78", "06", "E6", "E1", "40", "0A", "7D", "F4", "3C", "1F", "8E", "EA", "69", "00", "D7", "4B", "1C", "11", "0F", "08", "21", "AF", "F3", "B7", "A0", "F8", "73", "6E", "77", "B8", "B6", "25", "B4", "CA", "BD", "10", "38", "3A", "C0", "14", "91", "FB", "7A", "0D", "2E", "DF", "FD", "E9", "28", "3D", "96", "6A", "75", "ED", "EF", "A6", "E5", "70", "FF", "90", "55", "4C", "A9", "23", "F1", "88", "63", "83", "AE", "DD", "FA", "2F", "C9", "5A", "B0", "EE", "05", "89", "C3", "0E", "37", "D6", "24", "AB", "09", "E3", "E7", "D0", "A3", "5F", "B5", "F2", "BE", "CE", "36", "3E", "C1", "0B", "17", "22", "D4", "D9", "C5", "60", "C4", "BC", "E8", "A8", "0C", "D5", "9A", "9F", "FE", "53", "80", "FC", "62", "B9", "67", "F5", "35", "6C", "43", "2A", "EB", "1A", "5C", "CB", "34", "6D", "DC", "D2", "18", "74", "04", "7B", "13", "19", "6B", "29", "72", "30", "D3", "B2", "12", "8D", "65", "95", "15", "D1", "D8", "2B", "BB", "E2", "C7", "92", "9B", "51", "59", "A5", "C8", "47", "71", "32", "93", "64", "41", "DB", "56", "EC", "C6", "4E", "7C", "1B", "BF", "97", "58", "03", "B1", "2C", "57", "81", "4D", "7F", "DA", "A4", "F0", "87", "76", "85", "8A", "68", "99", "AC", "A2", "E0", "46", "DE", "4A", "9E", "27", "44", "8C", "49",
  //    "FD", "9E", "93", "72", "4D", "6D", "B7", "41", "91", "1C", "18", "6C", "36", "40", "42", "17", "C9", "D2", "50", "5A", "D3", "BD", "74", "B3", "00", "24", "84", "60", "19", "51", "5B", "2C", "D4", "2E", "8F", "39", "56", "2D", "3D", "CE", "9B", "1F", "2B", "E9", "67", "1B", "0A", "7D", "94", "C8", "44", "8A", "2A", "BB", "CF", "31", "95", "B5", "14", "07", "A1", "FC", "85", "26", "53", "6E", "ED", "87", "16", "5F", "E5", "57", "80", "E1", "D1", "2F", "B2", "11", "71", "FE", "08", "A6", "7F", "25", "F5", "8C", "4F", "C4", "0E", "B6", "73", "98", "0F", "A8", "C3", "78", "9A", "D8", "22", "AA", "5E", "EF", "62", "0D", "46", "C6", "C5", "3A", "1E", "27", "AE", "FF", "09", "52", "21", "23", "12", "20", "D6", "29", "7A", "32", "D5", "B9", "4E", "AB", "37", "3E", "D7", "43", "58", "3F", "55", "48", "64", "86", "F0", "34", "A5", "89", "97", "99", "A0", "15", "9C", "82", "7E", "7C", "79", "77", "1A", "0B", "CB", "F3", "3B", "66", "68", "01", "81", "59", "A4", "E2", "C1", "33", "1D", "C0", "B1", "C2", "DB", "AD", "DC", "EB", "DF", "4C", "CC", "6F", "96", "38", "F1", "F4", "C7", "54", "F8", "02", "EC", "70", "CD", "69", "D0", "75", "04", "E6", "63", "92", "B0", "FA", "6B", "B8", "BA", "4B", "47", "BE", "BF", "06", "13", "0C", "A3", "90", "9D", "F7", "65", "30", "5D", "EE", "6A", "B4", "03", "E4", "DD", "A7", "A9", "E7", "F2", "49", "76", "88", "FB", "EA", "5C", "DE", "DA", "8E", "05", "10", "3C", "BC", "E0", "F9", "D9", "7B", "E3", "E8", "AC", "9F", "4A", "45", "CA", "AF", "8D", "A2", "28", "F6", "35", "61", "83", "8B")

}
